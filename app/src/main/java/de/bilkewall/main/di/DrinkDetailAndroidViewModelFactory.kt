// DrinkDetailAndroidViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.viewmodel.DrinkDetailViewModel
import de.bilkewall.plugins.view.drinkDetail.DrinkDetailAndroidViewModel

class DrinkDetailAndroidViewModelFactory(
    private val drinkDetailViewModel: DrinkDetailViewModel,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DrinkDetailAndroidViewModel::class.java)) {
            val androidViewModel = DrinkDetailAndroidViewModel(drinkDetailViewModel)
            @Suppress("UNCHECKED_CAST")
            return androidViewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
