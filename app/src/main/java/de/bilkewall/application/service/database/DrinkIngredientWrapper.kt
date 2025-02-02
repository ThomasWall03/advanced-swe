package de.bilkewall.application.service.database

import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.domain.AppDrink

class DrinkIngredientWrapper(
    val drinkRepository: DrinkRepositoryInterface,
    val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface
) {
    suspend fun getDrinkById(id: Int): AppDrink {
        val drink = drinkRepository.getDrinkById(id)
        val ingredients = drinkIngredientCrossRefRepository.getIngredientsForDrink(id)
        return drink.copy(
            ingredients = ingredients.map { it.ingredientName },
            measurements = ingredients.map { it.unit }
        )
    }
}