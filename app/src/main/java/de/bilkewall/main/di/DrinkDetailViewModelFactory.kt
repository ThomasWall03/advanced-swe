// DrinkDetailViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.application.service.DrinkService
import de.bilkewall.adapters.viewmodel.DrinkDetailViewModel

class DrinkDetailViewModelFactory(
    private val drinkService: DrinkService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DrinkDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DrinkDetailViewModel(
                drinkService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}