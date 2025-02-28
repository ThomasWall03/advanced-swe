package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.viewmodel.MatchesViewModel
import de.bilkewall.application.service.DrinkFetchingService
import de.bilkewall.application.service.ProfileManagementService

class MatchesViewModelFactory(
    private var profileManagementService: ProfileManagementService,
    private var drinkFetchingService: DrinkFetchingService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MatchesViewModel(
                profileManagementService,
                drinkFetchingService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}