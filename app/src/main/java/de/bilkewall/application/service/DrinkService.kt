package de.bilkewall.application.service

import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.domain.Drink
import de.bilkewall.domain.DrinkTypeFilter
import de.bilkewall.domain.IngredientValueFilter
import de.bilkewall.domain.Match
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DrinkService private constructor(
    private val drinkRepository: DrinkRepositoryInterface,
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface
) {

    companion object {
        @Volatile
        private var instance: DrinkService? = null

        fun getInstance(
            drinkRepository: DrinkRepositoryInterface,
            drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface
        ): DrinkService {
            return instance ?: synchronized(this) {
                instance ?: DrinkService(drinkRepository, drinkIngredientCrossRefRepository).also {
                    instance = it
                }
            }
        }
    }

    private val _availableDrinks = MutableStateFlow<List<Drink>>(emptyList())
    val availableDrinks: StateFlow<List<Drink>> = _availableDrinks

    suspend fun getDrinkById(id: Int): Drink {
        val drink = drinkRepository.getDrinkById(id)
        val ingredients = drinkIngredientCrossRefRepository.getIngredientsForDrink(id)
        return drink.copy(
            ingredients = ingredients.map { it.ingredientName },
            measurements = ingredients.map { it.unit }
        )
    }

    fun getAllDrinks(): Flow<List<Drink>> {
        return drinkRepository.getAllDrinks().map { drinks ->
            drinks.map { drink ->
                getDrinkById(drink.drinkId)
            }
        }
    }

    suspend fun getDrinksByName(name: String): Flow<List<Drink>> {
        return drinkRepository.getDrinksByName(name).map { drinks ->
            drinks.map { drink ->
                getDrinkById(drink.drinkId)
            }
        }
    }

    suspend fun getDrinkCount() = drinkRepository.getDrinkCount()

    suspend fun getMatchedDrinksByName(name: String, profileId: Int): Flow<List<Drink>> {
        return drinkRepository.getMatchedDrinksByName(name, profileId).map { drinks ->
            drinks.map { drink ->
                getDrinkById(drink.drinkId)
            }
        }
    }

    suspend fun getMatchedDrinksForProfile(profileId: Int): Flow<List<Drink>> {
        return drinkRepository.getMatchedDrinksForProfile(profileId).map { drinks ->
            drinks.map { drink ->
                getDrinkById(drink.drinkId)
            }
        }
    }

    suspend fun evaluateCurrentDrink(
        bypassFilter: Boolean,
        matches: List<Match>,
        ingredientFilters: List<IngredientValueFilter>,
        drinkTypeFilters: List<DrinkTypeFilter>
    ): Drink {
        if (bypassFilter) {
            calculateAvailableDrinks(matches, emptyList(), emptyList())
        } else {
            calculateAvailableDrinks(matches, ingredientFilters, drinkTypeFilters)
        }
        return getDrinkById((_availableDrinks.value.firstOrNull() ?: Drink()).drinkId)
    }

    fun isAllDrinkMatched(bypassFilter: Boolean): Boolean {
        return _availableDrinks.value.isEmpty() && bypassFilter
    }

    private suspend fun calculateAvailableDrinks(
        matches: List<Match>,
        ingredientFilters: List<IngredientValueFilter>,
        drinkTypeFilters: List<DrinkTypeFilter>
    ) {
        val allDrinks = drinkRepository.getAllDrinks().first()

        val drinkTypeFilterValues = drinkTypeFilters.map { it.drinkTypeFilterValue }
        val ingredientFilterValues = ingredientFilters.map { it.ingredientFilterValue }

        val availableDrinks = allDrinks.filter { drink ->
            val hasValidCategory =
                drinkTypeFilterValues.isEmpty() || drinkTypeFilterValues.contains(drink.categoryName)

            val hasMatchingIngredient =
                ingredientFilterValues.isEmpty() || drinkIngredientCrossRefRepository
                    .getIngredientsForDrink(drink.drinkId)
                    .any { ingredient -> ingredientFilterValues.contains(ingredient.ingredientName) }

            val isNotMatched = matches.none { it.drinkId == drink.drinkId }

            hasValidCategory && hasMatchingIngredient && isNotMatched
        }

        _availableDrinks.value = availableDrinks
    }
}