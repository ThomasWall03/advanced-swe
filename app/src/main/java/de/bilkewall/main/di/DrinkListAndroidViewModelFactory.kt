package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.viewmodel.DrinkListViewModel
import de.bilkewall.plugins.view.drinkList.DrinkListAndroidViewModel

class DrinkListAndroidViewModelFactory(
    private val drinkListViewModel: DrinkListViewModel,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DrinkListAndroidViewModel::class.java)) {
            val androidViewModel = DrinkListAndroidViewModel(drinkListViewModel)
            @Suppress("UNCHECKED_CAST")
            return androidViewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
