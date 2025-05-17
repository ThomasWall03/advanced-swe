package de.bilkewall.application.repository.drinkingredientcrossref

import de.bilkewall.domain.DrinkIngredientCrossRef
import kotlinx.coroutines.flow.Flow

interface DrinkIngredientCrossRefFetchingInterface {
    fun getAllIngredientsSortedByName(): Flow<List<String>>

    suspend fun getIngredientsForDrink(drinkId: Int): List<DrinkIngredientCrossRef>
}
