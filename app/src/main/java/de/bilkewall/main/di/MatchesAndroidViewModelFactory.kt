package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.adapters.viewmodel.MatchesViewModel
import de.bilkewall.plugins.view.matches.MatchesAndroidViewModel

class MatchesAndroidViewModelFactory(
    private var matchesViewModel: MatchesViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchesAndroidViewModel::class.java)) {
            val androidViewModel = MatchesAndroidViewModel(matchesViewModel)
            @Suppress("UNCHECKED_CAST")
            return androidViewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
