package de.bilkewall.cinder.view.matches

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.cinder.database.drink.Drink
import de.bilkewall.cinder.database.match.Match
import de.bilkewall.cinder.di.DependencyProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MatchesViewModel : ViewModel() {
    private val matchRepository = DependencyProvider.matchRepository
    private val profileRepository = DependencyProvider.profileRepository
    private val drinkRepository = DependencyProvider.drinkRepository

    private val _visibleDrinks = MutableStateFlow<List<Drink>>(emptyList())
    val visibleDrinks: StateFlow<List<Drink>> get() = _visibleDrinks
    var errorMessage: String by mutableStateOf("")
    var loading: Boolean by mutableStateOf(false)

    var matchSearchText: String by mutableStateOf("")

    fun loadVisibleDrinks() = viewModelScope.launch(Dispatchers.IO) {
        val currentProfile = profileRepository.activeProfile.firstOrNull()
        if (currentProfile != null) {
            val matches = matchRepository.getAllPositiveMatchesForCurrentProfile(currentProfile.profileId)

            val drinksToDisplay = matches.mapNotNull { match ->
                try {
                    drinkRepository.getDrinkById(match.drinkId)
                } catch (e: Exception) {
                    Log.e("loadVisibleDrinks", "Failed to load drink for match ID: ${match.drinkId}")
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
            _visibleDrinks.value.filter { it.drinkName.contains(drinkSearchText, ignoreCase = true) }
    }
}