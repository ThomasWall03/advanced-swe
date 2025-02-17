package de.bilkewall.application.service

import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.domain.AppDrink
import de.bilkewall.domain.AppDrinkTypeFilter
import de.bilkewall.domain.AppIngredientValueFilter
import de.bilkewall.domain.AppMatch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DrinkService(
    private val drinkRepository: DrinkRepositoryInterface,
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface
) {

    private val _availableDrinks = MutableStateFlow<List<AppDrink>>(emptyList())
    val availableDrinks: StateFlow<List<AppDrink>> = _availableDrinks

    suspend fun getDrinkById(id: Int): AppDrink {
        val drink = drinkRepository.getDrinkById(id)
        val ingredients = drinkIngredientCrossRefRepository.getIngredientsForDrink(id)
        return drink.copy(
            ingredients = ingredients.map { it.ingredientName },
            measurements = ingredients.map { it.unit }
        )
    }

    fun getAllDrinks(): Flow<List<AppDrink>> {
        return drinkRepository.getAllDrinks().map { drinks ->
            drinks.map { drink ->
                getDrinkById(drink.drinkId)
            }
        }
    }

    suspend fun getDrinksByName(name: String): Flow<List<AppDrink>> {
        return drinkRepository.getDrinksByName(name).map { drinks ->
            drinks.map { drink ->
                getDrinkById(drink.drinkId)
            }
        }
    }

    suspend fun getDrinkCount() = drinkRepository.getDrinkCount()

    suspend fun getMatchedDrinksByName(name: String, profileId: Int): Flow<List<AppDrink>> {
        return drinkRepository.getMatchedDrinksByName(name, profileId).map { drinks ->
            drinks.map { drink ->
                getDrinkById(drink.drinkId)
            }
        }
    }

    suspend fun getMatchedDrinksForProfile(profileId: Int): Flow<List<AppDrink>> {
        return drinkRepository.getMatchedDrinksForProfile(profileId).map { drinks ->
            drinks.map { drink ->
                getDrinkById(drink.drinkId)
            }
        }
    }

    suspend fun evaluateCurrentDrink(
        bypassFilter: Boolean,
        matches: List<AppMatch>,
        ingredientFilters: List<AppIngredientValueFilter>,
        drinkTypeFilters: List<AppDrinkTypeFilter>
    ): AppDrink {
        if (bypassFilter) {
            calculateAvailableDrinks(matches, emptyList(), emptyList())
        } else {
            calculateAvailableDrinks(matches, ingredientFilters, drinkTypeFilters)
        }
        return getDrinkById((_availableDrinks.value.firstOrNull() ?: AppDrink()).drinkId)
    }

    fun isAllDrinkMatched(bypassFilter: Boolean): Boolean {
        return _availableDrinks.value.isEmpty() && bypassFilter
    }

    private suspend fun calculateAvailableDrinks(
        matches: List<AppMatch>,
        ingredientFilters: List<AppIngredientValueFilter>,
        drinkTypeFilters: List<AppDrinkTypeFilter>
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