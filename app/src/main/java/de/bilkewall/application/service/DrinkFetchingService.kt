package de.bilkewall.application.service

import de.bilkewall.application.repository.drink.DrinkRepositoryFetchingInterface
import de.bilkewall.application.repository.drinkingredientcrossref.DrinkIngredientCrossRefFetchingInterface
import de.bilkewall.domain.Drink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.reflect.typeOf

class DrinkFetchingService private constructor(
    private val drinkRepository: DrinkRepositoryFetchingInterface,
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefFetchingInterface,
) {
    companion object {
        @Volatile
        private var instance: DrinkFetchingService? = null

        fun getInstance(
            drinkRepository: DrinkRepositoryFetchingInterface,
            drinkIngredientCrossRefRepository: DrinkIngredientCrossRefFetchingInterface,
        ): DrinkFetchingService =
            instance ?: synchronized(this) {
                instance ?: DrinkFetchingService(
                    drinkRepository,
                    drinkIngredientCrossRefRepository,
                ).also {
                    instance = it
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
            measurements = ingredients.map { it.unit },
        )
    }

    fun getAllDrinks(): Flow<List<Drink>> =
        drinkRepository.getAllDrinks().map { drinks ->
            drinks.map { drink -> addIngredientsToDrink(drink) }
        }

    fun getDrinksByName(name: String): Flow<List<Drink>> =
        drinkRepository.getDrinksByName(name).map { drinks ->
            drinks.map { drink -> addIngredientsToDrink(drink) }
        }

    suspend fun getDrinkCount() = drinkRepository.getDrinkCount()

    fun getMatchedDrinksByNameAndProfile(
        name: String,
        profileId: Int,
    ): Flow<List<Drink>> {
        if (name.isEmpty()) {
            throw IllegalArgumentException("Name cannot be empty")
        }
        if (profileId < 0) {
            throw IllegalArgumentException("Profile ID cannot be negative")
        }
        return drinkRepository.getMatchedDrinksByName(name, profileId).map { drinks ->
            drinks.map { drink -> addIngredientsToDrink(drink) }
        }
    }

    fun getMatchedDrinksForProfile(profileId: Int): Flow<List<Drink>> =
        drinkRepository.getMatchedDrinksForProfile(profileId).map { drinks ->
            drinks.map { drink -> addIngredientsToDrink(drink) }
        }
}
