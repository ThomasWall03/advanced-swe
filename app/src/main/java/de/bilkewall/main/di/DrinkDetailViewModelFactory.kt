// DrinkDetailViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.DrinkIngredientWrapper
import de.bilkewall.adapters.viewmodel.DrinkDetailViewModel

class DrinkDetailViewModelFactory(
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface,
    private val drinkIngredientWrapper: DrinkIngredientWrapper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DrinkDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DrinkDetailViewModel(
                drinkIngredientCrossRefRepository,
                drinkIngredientWrapper
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}