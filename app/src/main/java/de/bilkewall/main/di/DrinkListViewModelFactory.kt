package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.viewmodel.DrinkListViewModel
import de.bilkewall.application.service.DrinkFetchingService

class DrinkListViewModelFactory(
    private val drinkFetchingService: DrinkFetchingService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DrinkListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DrinkListViewModel(drinkFetchingService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}