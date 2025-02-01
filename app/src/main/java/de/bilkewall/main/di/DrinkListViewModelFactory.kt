// DrinkListViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.repository.DrinkRepositoryInterface
import de.bilkewall.adapters.viewmodel.DrinkListViewModel

class DrinkListViewModelFactory(
    private val drinkRepository: DrinkRepositoryInterface
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DrinkListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DrinkListViewModel(drinkRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}