package de.bilkewall.application.service

import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.domain.Drink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DrinkFetchingService private constructor(
    private val drinkRepository: DrinkRepositoryInterface,
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface
) {
    companion object {
        @Volatile
        private var instance: DrinkFetchingService? = null

        fun getInstance(
            drinkRepository: DrinkRepositoryInterface,
            drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface
        ): DrinkFetchingService {
            return instance ?: synchronized(this) {
                instance ?: DrinkFetchingService(
                    drinkRepository,
                    drinkIngredientCrossRefRepository
                ).also {
                    instance = it
                }
            }
        }
    }

    suspend fun getDrinkById(id: Int): Drink {
        val drink = drinkRepository.getDrinkById(id)
        return addIngredientsToDrink(drink)
    }

    private suspend fun addIngredientsToDrink(drink: Drink): Drink {
        val ingredients = drinkIngredientCrossRefRepository.getIngredientsForDrink(drink.drinkId)
        return drink.copy(
            ingredients = ingredients.map { it.ingredientName },
            measurements = ingredients.map { it.unit }
        )
    }

    fun getAllDrinks(): Flow<List<Drink>> {
        return drinkRepository.getAllDrinks().map { drinks ->
            drinks.map { drink -> addIngredientsToDrink(drink) }
        }
    }

    fun getDrinksByName(name: String): Flow<List<Drink>> {
        return drinkRepository.getDrinksByName(name).map { drinks ->
            drinks.map { drink -> addIngredientsToDrink(drink) }
        }
    }

    suspend fun getDrinkCount() = drinkRepository.getDrinkCount()

    fun getMatchedDrinksByNameAndProfile(name: String, profileId: Int): Flow<List<Drink>> {
        return drinkRepository.getMatchedDrinksByName(name, profileId).map { drinks ->
            drinks.map { drink -> addIngredientsToDrink(drink) }
        }
    }

    fun getMatchedDrinksForProfile(profileId: Int): Flow<List<Drink>> {
        return drinkRepository.getMatchedDrinksForProfile(profileId).map { drinks ->
            drinks.map { drink -> addIngredientsToDrink(drink) }
        }
    }
}