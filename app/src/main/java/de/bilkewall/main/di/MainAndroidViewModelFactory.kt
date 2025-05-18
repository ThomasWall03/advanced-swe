package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.viewmodel.MainViewModel
import de.bilkewall.plugins.view.main.MainAndroidViewModel

class MainAndroidViewModelFactory(
    private val mainViewModel: MainViewModel,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainAndroidViewModel::class.java)) {
            val androidViewModel = MainAndroidViewModel(mainViewModel)
            @Suppress("UNCHECKED_CAST")
            return androidViewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
