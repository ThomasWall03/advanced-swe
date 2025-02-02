package de.bilkewall.application.service

import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface

class IngredientService (
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface
) {
    fun getAllIngredientsSortedByName() = drinkIngredientCrossRefRepository.getAllIngredientsSortedByName()
}