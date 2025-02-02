// CreateProfileViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.application.repository.SharedFilterRepositoryInterface
import de.bilkewall.application.service.api.ApiService
import de.bilkewall.adapters.viewmodel.CreateProfileViewModel
import de.bilkewall.application.service.database.CreateProfileService

class CreateProfileViewModelFactory(
    private val apiService: ApiService,
    private val createProfileService: CreateProfileService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateProfileViewModel(
                apiService,
                createProfileService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}