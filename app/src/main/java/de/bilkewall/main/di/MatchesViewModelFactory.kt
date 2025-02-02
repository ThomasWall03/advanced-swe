// MatchesViewModelFactory.kt
package de.bilkewall.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.application.repository.MatchRepositoryInterface
import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.adapters.viewmodel.MatchesViewModel

class MatchesViewModelFactory(
    private val matchRepository: MatchRepositoryInterface,
    private val profileRepository: ProfileRepositoryInterface,
    private val drinkRepository: DrinkRepositoryInterface
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MatchesViewModel(
                matchRepository,
                profileRepository,
                drinkRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}