package de.bilkewall.adapters.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.application.repository.MatchRepositoryInterface
import de.bilkewall.application.service.database.DrinkService
import de.bilkewall.application.service.database.ProfileService
import de.bilkewall.domain.AppDrink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MatchesViewModel(
    private var profileService: ProfileService,
    private var drinkService: DrinkService
) : ViewModel() {

    private val _visibleDrinks = MutableStateFlow<List<AppDrink>>(emptyList())
    val visibleDrinks: StateFlow<List<AppDrink>> get() = _visibleDrinks
    var errorMessage: String by mutableStateOf("")
    var loading: Boolean by mutableStateOf(false)

    var matchSearchText: String by mutableStateOf("")

    fun loadMatchedDrinks() = viewModelScope.launch(Dispatchers.IO) {
        val currentProfile = profileService.getActiveProfile().firstOrNull()
        if (currentProfile != null) {
            _visibleDrinks.value = drinkService.getMatchedDrinksForProfile(currentProfile.profileId).first()
        } else {
            Log.e("loadVisibleDrinks", "No active profile found.")
        }
    }

    fun getMatchesByName(drinkSearchText: String) {
        viewModelScope.launch {
            val currentProfile = profileService.getActiveProfile().firstOrNull()
            errorMessage = ""
            loading = true
            try {
                if (currentProfile != null) {
                    _visibleDrinks.value = drinkService.getMatchedDrinksByName(drinkSearchText, currentProfile.profileId).first()
                } else {
                    Log.e("getMatchesByName", "No active profile found.")
                }
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
            loading = false
        }
    }
}