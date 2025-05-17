package de.bilkewall.application.service

import de.bilkewall.application.repository.drinkingredientcrossref.DrinkIngredientCrossRefFetchingInterface

class IngredientService private constructor(
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefFetchingInterface,
) {
    companion object {
        @Volatile
        private var instance: IngredientService? = null

        fun getInstance(drinkIngredientCrossRefRepository: DrinkIngredientCrossRefFetchingInterface): IngredientService =
            instance ?: synchronized(this) {
                instance ?: IngredientService(drinkIngredientCrossRefRepository).also {
                    instance = it
                }
            }
    }

    fun getAllIngredientsSortedByName() = drinkIngredientCrossRefRepository.getAllIngredientsSortedByName()
}
