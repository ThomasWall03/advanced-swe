package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.adapters.repository.DrinkIngredientWrapper
import de.bilkewall.adapters.repository.DrinkRepositoryInterface
import de.bilkewall.adapters.repository.MatchRepositoryInterface
import de.bilkewall.adapters.repository.ProfileRepositoryInterface
import de.bilkewall.adapters.repository.SharedFilterRepositoryInterface
import de.bilkewall.adapters.service.DrinkService
import de.bilkewall.adapters.viewmodel.MainViewModel

class MainViewModelFactory(
    private val sharedFilterRepository: SharedFilterRepositoryInterface,
    private val profileRepository: ProfileRepositoryInterface,
    private val matchRepository: MatchRepositoryInterface,
    private val drinkRepository: DrinkRepositoryInterface,
    private val drinkWrapper: DrinkIngredientWrapper,
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface,
    private val drinkService: DrinkService
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
                drinkService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}