package de.bilkewall.adapters.viewmodel

import de.bilkewall.application.service.DrinkFetchingService
import de.bilkewall.application.service.DrinkFilterService
import de.bilkewall.application.service.MatchService
import de.bilkewall.application.service.ProfileManagementService
import de.bilkewall.application.service.SharedFilterService
import de.bilkewall.domain.Drink
import de.bilkewall.domain.Match
import de.bilkewall.domain.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull

class MainViewModel(
    private val profileManagementService: ProfileManagementService,
    private val matchService: MatchService,
    private val drinkFilterService: DrinkFilterService,
    private val drinkFetchingService: DrinkFetchingService,
    private val sharedFilterService: SharedFilterService,
) {
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    val allProfiles: Flow<List<Profile>> = profileManagementService.allProfiles
    val currentProfile = profileManagementService.getActiveProfile()

    private val _currentDrink = MutableStateFlow(Drink())
    val currentDrink: StateFlow<Drink> get() = _currentDrink

    private var bypassFilter = false
    private var matchingActive = true
    private val _isInitialLoad = MutableStateFlow(true)
    val isInitialLoad: StateFlow<Boolean> get() = _isInitialLoad

    private val _allDrinksMatched = MutableStateFlow(false)
    val allDrinksMatched: StateFlow<Boolean> get() = _allDrinksMatched

    private val _noMoreDrinksAvailable = MutableStateFlow(false)
    val noMoreDrinksAvailable: StateFlow<Boolean> get() = _noMoreDrinksAvailable

    suspend fun evaluateCurrentDrink() {
        _isInitialLoad.value = true
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
                        bypassFilter,
                        matchService.getMatchesForProfile(profile.profileId),
                        filters,
                    )
                _allDrinksMatched.value =
                    drinkFilterService.areAllDrinksMatched(bypassFilter, filteredDrinks)
                _noMoreDrinksAvailable.value =
                    drinkFilterService.noMoreDrinksAvailable(filteredDrinks)
                if (_noMoreDrinksAvailable.value) {
                    _currentDrink.value = Drink()
                } else {
                    _currentDrink.value =
                        drinkFetchingService.getDrinkById(filteredDrinks.first().drinkId)
                }
            }
        } catch (e: Exception) {
            _errorMessage.value = "Error: ${e.message}"
        } finally {
            matchingActive = true
            _isInitialLoad.value = false
            _isLoading.value = false
        }
    }

    fun toggleFilterBypass(byPassFilter: Boolean) {
        bypassFilter = byPassFilter
    }

    suspend fun setCurrentProfile(profile: Profile) {
        profileManagementService.setCurrentProfile(profile)

        bypassFilter = false
        _allDrinksMatched.value = false
        _isInitialLoad.value = true
        evaluateCurrentDrink()
    }

    suspend fun handleMatchingRequest(
        match: Boolean,
        drinkId: Int,
        profileId: Int,
    ) {
        if (matchingActive) {
            matchService.insert(Match(drinkId, profileId, match))
            matchingActive = false
            evaluateCurrentDrink()
        }
    }

    suspend fun deleteProfile(profile: Profile) {
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
