// DrinkListViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.adapters.viewmodel.DrinkListViewModel
import de.bilkewall.application.service.database.DrinkService
import de.bilkewall.main.di.DependencyProvider.drinkRepository
import de.bilkewall.main.di.DependencyProvider.getAllDrinksService
import de.bilkewall.main.di.DependencyProvider.getDrinksByNameService

class DrinkListViewModelFactory(
    private val drinkService: DrinkService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DrinkListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DrinkListViewModel(drinkService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}