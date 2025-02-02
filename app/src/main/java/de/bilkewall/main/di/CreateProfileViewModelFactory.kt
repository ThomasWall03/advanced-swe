// CreateProfileViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.viewmodel.CreateProfileViewModel
import de.bilkewall.application.service.CategoryService
import de.bilkewall.application.service.IngredientService
import de.bilkewall.application.service.ProfileService

class CreateProfileViewModelFactory(
    private val profileService: ProfileService,
    private val ingredientService: IngredientService,
    private val categoryService: CategoryService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateProfileViewModel(
                profileService,
                ingredientService,
                categoryService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}