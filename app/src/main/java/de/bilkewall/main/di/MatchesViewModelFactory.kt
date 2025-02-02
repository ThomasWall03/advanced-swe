package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.application.repository.MatchRepositoryInterface
import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.adapters.viewmodel.MatchesViewModel
import de.bilkewall.application.service.database.DrinkService
import de.bilkewall.application.service.database.ProfileService
import de.bilkewall.main.di.DependencyProvider.drinkService

class MatchesViewModelFactory(
    private var profileService: ProfileService,
    private var drinkService: DrinkService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MatchesViewModel(
                profileService,
                drinkService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}