package de.bilkewall.application.service.database

import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.domain.AppDrink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DrinkService(
    val drinkRepository: DrinkRepositoryInterface,
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface
) {
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

    suspend fun getMatchedDrinksByName(name: String): Flow<List<AppDrink>> {
        return drinkRepository.getMatchedDrinksByName(name).map { drinks ->
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
}