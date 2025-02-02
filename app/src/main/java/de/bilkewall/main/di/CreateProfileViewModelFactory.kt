// CreateProfileViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.application.repository.SharedFilterRepositoryInterface
import de.bilkewall.application.service.api.ApiService
import de.bilkewall.adapters.viewmodel.CreateProfileViewModel

class CreateProfileViewModelFactory(
    private val profileRepository: ProfileRepositoryInterface,
    private val sharedFilterRepository: SharedFilterRepositoryInterface,
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface,
    private val apiService: ApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateProfileViewModel(
                profileRepository,
                sharedFilterRepository,
                drinkIngredientCrossRefRepository,
                apiService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}