// CreateProfileViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.repository.*
import de.bilkewall.adapters.service.DrinkService
import de.bilkewall.adapters.viewmodel.CreateProfileViewModel

class CreateProfileViewModelFactory(
    private val profileRepository: ProfileRepositoryInterface,
    private val sharedFilterRepository: SharedFilterRepositoryInterface,
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface,
    private val drinkService: DrinkService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateProfileViewModel(
                profileRepository,
                sharedFilterRepository,
                drinkIngredientCrossRefRepository,
                drinkService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}