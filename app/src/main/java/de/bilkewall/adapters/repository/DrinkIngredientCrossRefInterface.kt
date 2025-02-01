package de.bilkewall.adapters.repository

import de.bilkewall.domain.AppDrinkIngredientCrossRef
import kotlinx.coroutines.flow.Flow

interface DrinkIngredientCrossRefInterface {
    fun getAllIngredientsSortedByName(): Flow<List<String>>

    suspend fun insert(drinkIngredientCrossRef: AppDrinkIngredientCrossRef)

    suspend fun deleteAllRelationsOfADrink(drinkId: Int)

    suspend fun deleteAllRelations()

    suspend fun getIngredientsForDrink(drinkId: Int): List<AppDrinkIngredientCrossRef>
}