package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.service.DrinkService
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.application.repository.MatchRepositoryInterface
import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.application.repository.SharedFilterRepositoryInterface
import de.bilkewall.adapters.viewmodel.MainViewModel
import de.bilkewall.application.service.MatchService
import de.bilkewall.application.service.ProfileService
import de.bilkewall.application.service.SharedFilterService

class MainViewModelFactory(
    private val drinkService: DrinkService,
    private val profileService: ProfileService,
    private val matchService: MatchService,
    private val sharedFilterService: SharedFilterService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                profileService,
                matchService,
                drinkService,
                sharedFilterService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}