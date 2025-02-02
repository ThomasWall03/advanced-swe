package de.bilkewall.adapters.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.application.repository.MatchRepositoryInterface
import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.domain.AppDrink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MatchesViewModel(
    private var matchRepository: MatchRepositoryInterface,
    private var profileRepository: ProfileRepositoryInterface,
    private var drinkRepository: DrinkRepositoryInterface
) : ViewModel() {

    private val _visibleDrinks = MutableStateFlow<List<AppDrink>>(emptyList())
    val visibleDrinks: StateFlow<List<AppDrink>> get() = _visibleDrinks
    var errorMessage: String by mutableStateOf("")
    var loading: Boolean by mutableStateOf(false)

    var matchSearchText: String by mutableStateOf("")

    fun loadVisibleDrinks() = viewModelScope.launch(Dispatchers.IO) {
        val currentProfile = profileRepository.activeProfile.firstOrNull()
        if (currentProfile != null) {
            val matches =
                matchRepository.getAllPositiveMatchesForCurrentProfile(currentProfile.profileId)

            val drinksToDisplay = matches.mapNotNull { match ->
                try {
                    drinkRepository.getDrinkById(match.drinkId)
                } catch (e: Exception) {
                    Log.e(
                        "loadVisibleDrinks",
                        "Failed to load drink for match ID: ${match.drinkId}"
                    )
                    null
                }
            }

            _visibleDrinks.value = drinksToDisplay
        } else {
            Log.e("loadVisibleDrinks", "No active profile found.")
        }
    }

    fun getMatchesByName(drinkSearchText: String) {
        _visibleDrinks.value =
            _visibleDrinks.value.filter {
                it.drinkName.contains(
                    drinkSearchText,
                    ignoreCase = true
                )
            }
    }
}