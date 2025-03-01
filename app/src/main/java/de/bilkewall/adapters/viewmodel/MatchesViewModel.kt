package de.bilkewall.adapters.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.application.service.DrinkFetchingService
import de.bilkewall.application.service.ProfileManagementService
import de.bilkewall.domain.Drink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MatchesViewModel(
    private var profileManagementService: ProfileManagementService,
    private var drinkFetchingService: DrinkFetchingService,
) : ViewModel() {
    private val _visibleDrinks = MutableStateFlow<List<Drink>>(emptyList())
    val visibleDrinks: StateFlow<List<Drink>> get() = _visibleDrinks
    var errorMessage: String by mutableStateOf("")
    var loading: Boolean by mutableStateOf(false)

    var matchSearchText: String by mutableStateOf("")

    fun loadMatchedDrinks() =
        viewModelScope.launch(Dispatchers.IO) {
            val currentProfile = profileManagementService.getActiveProfile().firstOrNull()
            if (currentProfile != null) {
                _visibleDrinks.value = drinkFetchingService.getMatchedDrinksForProfile(currentProfile.profileId).first()
            } else {
                Log.e("loadVisibleDrinks", "No active profile found.")
            }
        }

    fun getMatchesByName(drinkSearchText: String) {
        viewModelScope.launch {
            val currentProfile = profileManagementService.getActiveProfile().firstOrNull()
            errorMessage = ""
            loading = true
            try {
                if (currentProfile != null) {
                    _visibleDrinks.value =
                        drinkFetchingService.getMatchedDrinksByNameAndProfile(drinkSearchText, currentProfile.profileId).first()
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
