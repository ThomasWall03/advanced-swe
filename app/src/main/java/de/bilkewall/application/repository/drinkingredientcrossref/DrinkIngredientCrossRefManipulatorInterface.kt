package de.bilkewall.application.repository.drinkingredientcrossref

import de.bilkewall.domain.DrinkIngredientCrossRef

interface DrinkIngredientCrossRefManipulatorInterface {
    suspend fun insert(drinkIngredientCrossRef: DrinkIngredientCrossRef)

    suspend fun deleteAllRelationsOfADrink(drinkId: Int)

    suspend fun deleteAllRelations()
}