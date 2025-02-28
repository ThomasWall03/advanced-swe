package de.bilkewall.application.service

import de.bilkewall.domain.Drink
import de.bilkewall.domain.DrinkFilterStrategy
import de.bilkewall.domain.Match
import kotlinx.coroutines.flow.first

class DrinkFilterService private constructor(
    private val drinkFetchingService: DrinkFetchingService
) {
    companion object {
        @Volatile
        private var instance: DrinkFilterService? = null

        fun getInstance(
            drinkFetchingService: DrinkFetchingService
        ): DrinkFilterService {
            return instance ?: synchronized(this) {
                instance ?: DrinkFilterService(
                    drinkFetchingService
                ).also {
                    instance = it
                }
            }
        }
    }

    suspend fun evaluateCurrentDrinks(
        bypassFilter: Boolean,
        matches: List<Match>,
        filters: List<List<DrinkFilterStrategy>>
    ): List<Drink> {
        return if (bypassFilter) {
            filterDrinks(matches, emptyList())
        } else {
            filterDrinks(matches, filters)
        }
    }

    private suspend fun filterDrinks(
        matches: List<Match>,
        filters: List<List<DrinkFilterStrategy>>
    ): List<Drink> {
        val allDrinks = drinkFetchingService.getAllDrinks().first()

        return allDrinks.filter { drink ->
            val isNotMatched = matches.none { it.drinkId == drink.drinkId }
            val passesAtLeastOneInEachFilterList = filters.all { filterList ->
                filterList.any { it.apply(drink) }
            }

            isNotMatched && passesAtLeastOneInEachFilterList
        }
    }

    fun areAllDrinksMatched(bypassFilter: Boolean, filteredDrinks: List<Drink>): Boolean {
        return filteredDrinks.isEmpty() && bypassFilter
    }

    fun noMoreDrinksAvailable(filteredDrinks: List<Drink>): Boolean {
        return filteredDrinks.isEmpty()
    }
}