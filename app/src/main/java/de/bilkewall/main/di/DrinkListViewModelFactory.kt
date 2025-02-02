package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.viewmodel.DrinkListViewModel
import de.bilkewall.application.service.DrinkService

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