// LandingPageViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.application.service.api.ApiService
import de.bilkewall.adapters.viewmodel.LandingPageViewModel
import de.bilkewall.application.service.database.DrinkService
import de.bilkewall.application.service.database.LandingPageService

class LandingPageViewModelFactory(
    private val apiService: ApiService,
    private val landingPageService: LandingPageService,
    private val drinkService: DrinkService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LandingPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LandingPageViewModel(
                apiService,
                landingPageService,
                drinkService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}