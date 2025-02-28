package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.viewmodel.MainViewModel
import de.bilkewall.application.service.DrinkFetchingService
import de.bilkewall.application.service.DrinkFilterService
import de.bilkewall.application.service.MatchService
import de.bilkewall.application.service.ProfileManagementService
import de.bilkewall.application.service.SharedFilterService

class MainViewModelFactory(
    private val drinkFilterService: DrinkFilterService,
    private val profileManagementService: ProfileManagementService,
    private val matchService: MatchService,
    private val sharedFilterService: SharedFilterService,
    private val drinkFetchingService: DrinkFetchingService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                profileManagementService,
                matchService,
                drinkFilterService,
                drinkFetchingService,
                sharedFilterService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}