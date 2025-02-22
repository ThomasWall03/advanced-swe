package de.bilkewall.adapters.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.application.service.DrinkService
import de.bilkewall.application.service.MatchService
import de.bilkewall.application.service.ProfileService
import de.bilkewall.application.service.SharedFilterService
import de.bilkewall.domain.Drink
import de.bilkewall.domain.Match
import de.bilkewall.domain.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainViewModel(
    private val profileService: ProfileService,
    private val matchService: MatchService,
    private val drinkService: DrinkService,
    private val sharedFilterService: SharedFilterService
) : ViewModel() {
    val allProfiles: Flow<List<Profile>> = profileService.allProfiles
    val currentProfile = profileService.getActiveProfile()

    val availableDrinks: StateFlow<List<Drink>> = drinkService.availableDrinks

    private val _currentDrink = MutableStateFlow(Drink())
    val currentDrink: StateFlow<Drink> get() = _currentDrink

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading
    var isInitialLoad = mutableStateOf(true)

    private val bypassFilter = mutableStateOf(false)
    val allDrinksMatched = mutableStateOf(false)

    fun evaluateCurrentDrink() = viewModelScope.launch(Dispatchers.IO) {
        _loading.value = true
        try {
            val profile = currentProfile.firstOrNull()
            if(profile!=null){
                _currentDrink.value = drinkService.evaluateCurrentDrink(
                    bypassFilter.value,
                    matchService.getMatchesForProfile(profile.profileId),
                    sharedFilterService.getIngredientFilterValues(profile.profileId),
                    sharedFilterService.getDrinkTypeFilterValues(profile.profileId)
                )
                allDrinksMatched.value = drinkService.isAllDrinkMatched(bypassFilter.value)
            }

        } catch (e: Exception) {
            Log.e("MainViewModel.evaluateCurrentDrink", "Error: ${e.message}")
        } finally {
            isInitialLoad.value = false
            _loading.value = false
        }
    }

    fun toggleFilterBypass(byPassFilter: Boolean) {
        bypassFilter.value = byPassFilter
    }

    fun setCurrentProfile(profile: Profile) = viewModelScope.launch {
        profileService.setCurrentProfile(profile)

        bypassFilter.value = false
        allDrinksMatched.value = false
        isInitialLoad.value = true
        evaluateCurrentDrink()
    }

    fun handleMatchingRequest(match: Boolean, drinkId: Int, profileId: Int) =
        viewModelScope.launch {
            matchService.insert(Match(drinkId, profileId, match))
            evaluateCurrentDrink()
        }

    fun deleteProfile(profile: Profile) = viewModelScope.launch(Dispatchers.IO) {
        profileService.deleteProfile(profile)
        setCurrentProfile(profileService.allProfiles.first().first())
    }
}