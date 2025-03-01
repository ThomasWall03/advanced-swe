package de.bilkewall.application.repository

import de.bilkewall.domain.DrinkIngredientCrossRef
import kotlinx.coroutines.flow.Flow

interface DrinkIngredientCrossRefInterface {
    fun getAllIngredientsSortedByName(): Flow<List<String>>

    suspend fun insert(drinkIngredientCrossRef: DrinkIngredientCrossRef)

    suspend fun deleteAllRelationsOfADrink(drinkId: Int)

    suspend fun deleteAllRelations()

    suspend fun getIngredientsForDrink(drinkId: Int): List<DrinkIngredientCrossRef>
}
