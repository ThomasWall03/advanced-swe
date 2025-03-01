package de.bilkewall.adapters.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.application.service.DrinkFetchingService
import de.bilkewall.application.service.DrinkFilterService
import de.bilkewall.application.service.MatchService
import de.bilkewall.application.service.ProfileManagementService
import de.bilkewall.application.service.SharedFilterService
import de.bilkewall.domain.Drink
import de.bilkewall.domain.Match
import de.bilkewall.domain.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainViewModel(
    private val profileManagementService: ProfileManagementService,
    private val matchService: MatchService,
    private val drinkFilterService: DrinkFilterService,
    private val drinkFetchingService: DrinkFetchingService,
    private val sharedFilterService: SharedFilterService,
) : ViewModel() {
    val allProfiles: Flow<List<Profile>> = profileManagementService.allProfiles
    val currentProfile = profileManagementService.getActiveProfile()

    private val _currentDrink = MutableStateFlow(Drink())
    val currentDrink: StateFlow<Drink> get() = _currentDrink

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading
    var isInitialLoad = mutableStateOf(true)

    private val bypassFilter = mutableStateOf(false)
    val allDrinksMatched = mutableStateOf(false)
    val noMoreDrinksAvailable = mutableStateOf(false)

    fun evaluateCurrentDrink() =
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            try {
                val profile = currentProfile.firstOrNull()
                if (profile != null) {
                    val filters =
                        listOf(
                            sharedFilterService.getIngredientFilterValues(profile.profileId),
                            sharedFilterService.getDrinkTypeFilterValues(profile.profileId),
                        )
                    val filteredDrinks =
                        drinkFilterService.evaluateCurrentDrinks(
                            bypassFilter.value,
                            matchService.getMatchesForProfile(profile.profileId),
                            filters,
                        )
                    allDrinksMatched.value = drinkFilterService.areAllDrinksMatched(bypassFilter.value, filteredDrinks)
                    noMoreDrinksAvailable.value = drinkFilterService.noMoreDrinksAvailable(filteredDrinks)
                    if (noMoreDrinksAvailable.value) {
                        _currentDrink.value = Drink()
                    } else {
                        _currentDrink.value = drinkFetchingService.getDrinkById(filteredDrinks.first().drinkId)
                    }
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

    fun setCurrentProfile(profile: Profile) =
        viewModelScope.launch {
            profileManagementService.setCurrentProfile(profile)

            bypassFilter.value = false
            allDrinksMatched.value = false
            isInitialLoad.value = true
            evaluateCurrentDrink()
        }

    fun handleMatchingRequest(
        match: Boolean,
        drinkId: Int,
        profileId: Int,
    ) = viewModelScope.launch {
        matchService.insert(Match(drinkId, profileId, match))
        evaluateCurrentDrink()
    }

    fun deleteProfile(profile: Profile) =
        viewModelScope.launch(Dispatchers.IO) {
            sharedFilterService.deleteFiltersForProfile(profile.profileId)
            matchService.deleteMatchesForProfile(profile.profileId)
            profileManagementService.deleteProfile(profile)

            if (profile.isActiveProfile) {
                val firstProfile = allProfiles.firstOrNull()?.first()
                firstProfile?.let {
                    setCurrentProfile(it)
                }
            }
        }
}
