// LandingPageViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.repository.*
import de.bilkewall.adapters.service.DrinkService
import de.bilkewall.adapters.viewmodel.LandingPageViewModel

class LandingPageViewModelFactory(
    private val drinkRepository: DrinkRepositoryInterface,
    private val drinkService: DrinkService,
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface,
    private val profileRepository: ProfileRepositoryInterface
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LandingPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LandingPageViewModel(
                drinkRepository,
                drinkService,
                drinkIngredientCrossRefRepository,
                profileRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}