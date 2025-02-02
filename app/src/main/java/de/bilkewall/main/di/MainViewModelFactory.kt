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

class MainViewModelFactory(
    private val sharedFilterRepository: SharedFilterRepositoryInterface,
    private val profileRepository: ProfileRepositoryInterface,
    private val matchRepository: MatchRepositoryInterface,
    private val drinkRepository: DrinkRepositoryInterface,
    private val drinkWrapper: DrinkService,
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface,
    private val profileService: ProfileService,
    private val matchService: MatchService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                sharedFilterRepository,
                profileRepository,
                matchRepository,
                drinkRepository,
                drinkWrapper,
                drinkIngredientCrossRefRepository,
                profileService,
                matchService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}