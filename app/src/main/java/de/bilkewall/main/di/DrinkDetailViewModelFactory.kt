// DrinkDetailViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.viewmodel.DrinkDetailViewModel
import de.bilkewall.application.service.DrinkFetchingService

class DrinkDetailViewModelFactory(
    private val drinkFetchingService: DrinkFetchingService,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DrinkDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DrinkDetailViewModel(
                drinkFetchingService,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
