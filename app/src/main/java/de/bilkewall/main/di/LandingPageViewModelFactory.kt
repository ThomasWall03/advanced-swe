// LandingPageViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.DatabasePopulator
import de.bilkewall.adapters.viewmodel.LandingPageViewModel
import de.bilkewall.application.service.DrinkService
import de.bilkewall.application.service.ProfileService

class LandingPageViewModelFactory(
    private val drinkService: DrinkService,
    private val profileService: ProfileService,
    private val populator: DatabasePopulator
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LandingPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LandingPageViewModel(
                drinkService,
                profileService,
                populator
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}