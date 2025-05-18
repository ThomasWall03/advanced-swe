package de.bilkewall.adapters.viewmodel

import de.bilkewall.application.service.DrinkFetchingService
import de.bilkewall.application.service.ProfileManagementService
import de.bilkewall.domain.Drink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class MatchesViewModel(
    private var profileManagementService: ProfileManagementService,
    private var drinkFetchingService: DrinkFetchingService,
) {
    private val _visibleDrinks = MutableStateFlow<List<Drink>>(emptyList())
    val visibleDrinks: StateFlow<List<Drink>> get() = _visibleDrinks

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    suspend fun loadMatchedDrinks() {
        val currentProfile = profileManagementService.getActiveProfile().firstOrNull()
        if (currentProfile != null) {
            _visibleDrinks.value =
                drinkFetchingService.getMatchedDrinksForProfile(currentProfile.profileId).first()
        } else {
            _errorMessage.value = "No active profile found."
        }
    }

    suspend fun getMatchesByName(drinkSearchText: String) {
        val currentProfile = profileManagementService.getActiveProfile().firstOrNull()
        _errorMessage.value = ""
        _isLoading.value = true
        try {
            if (currentProfile != null) {
                _visibleDrinks.value =
                    drinkFetchingService.getMatchedDrinksByNameAndProfile(
                        drinkSearchText,
                        currentProfile.profileId
                    ).first()
            } else {
                _errorMessage.value = "No active profile found."
            }
        } catch (e: Exception) {
            _errorMessage.value = e.message.toString()
        } finally {
            _isLoading.value = false
        }
    }
}
