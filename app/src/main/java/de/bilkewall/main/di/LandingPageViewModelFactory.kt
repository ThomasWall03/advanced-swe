// LandingPageViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.DatabasePopulator
import de.bilkewall.adapters.viewmodel.LandingPageViewModel
import de.bilkewall.application.service.DrinkFetchingService
import de.bilkewall.application.service.ProfileManagementService

class LandingPageViewModelFactory(
    private val drinkFetchingService: DrinkFetchingService,
    private val profileManagementService: ProfileManagementService,
    private val populator: DatabasePopulator,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LandingPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LandingPageViewModel(
                drinkFetchingService,
                profileManagementService,
                populator,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
