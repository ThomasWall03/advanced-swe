package de.bilkewall.application.service

import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface

class IngredientService private constructor(
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface,
) {
    companion object {
        @Volatile
        private var instance: IngredientService? = null

        fun getInstance(drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface): IngredientService =
            instance ?: synchronized(this) {
                instance ?: IngredientService(drinkIngredientCrossRefRepository).also {
                    instance = it
                }
            }
    }

    fun getAllIngredientsSortedByName() = drinkIngredientCrossRefRepository.getAllIngredientsSortedByName()
}
