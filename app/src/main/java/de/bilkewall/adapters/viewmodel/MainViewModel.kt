package de.bilkewall.adapters.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.service.DrinkService
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.application.repository.MatchRepositoryInterface
import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.application.repository.SharedFilterRepositoryInterface
import de.bilkewall.application.service.MatchService
import de.bilkewall.application.service.ProfileService
import de.bilkewall.domain.AppDrink
import de.bilkewall.domain.AppDrinkTypeFilter
import de.bilkewall.domain.AppIngredientValueFilter
import de.bilkewall.domain.AppMatch
import de.bilkewall.domain.AppProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainViewModel(
    private var sharedFilterRepository: SharedFilterRepositoryInterface,
    private var profileRepository: ProfileRepositoryInterface,
    private var matchRepository: MatchRepositoryInterface,
    private var drinkRepository: DrinkRepositoryInterface,
    private var drinkWrapper: DrinkService,
    private var drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface,
    private val profileService: ProfileService,
    private val matchService: MatchService
) : ViewModel() {
    val allProfiles: Flow<List<AppProfile>> = profileRepository.allProfiles
    val currentProfile = profileRepository.activeProfile

    private val _availableDrinks = MutableStateFlow<List<AppDrink>>(emptyList())
    val availableDrinks: StateFlow<List<AppDrink>> = _availableDrinks

    private val _currentDrink = MutableStateFlow(AppDrink())
    val currentDrink: StateFlow<AppDrink> get() = _currentDrink

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading
    var isInitialLoad = mutableStateOf(true)

    private val bypassFilter = mutableStateOf(false)
    val allDrinksMatched = mutableStateOf(false)

    fun evaluateCurrentDrink() = viewModelScope.launch(Dispatchers.IO) {
        _loading.value = true
        try {
            val currentProfile = profileRepository.activeProfile.firstOrNull()
            if (currentProfile != null) {
                val matches =
                    matchRepository.getAllMatchesForCurrentProfile(currentProfile.profileId)
                if (bypassFilter.value) {
                    calculateAvailableDrinks(matches, emptyList(), emptyList())
                } else {
                    val ingredientFilters =
                        sharedFilterRepository.getIngredientValueFiltersByProfileId(currentProfile.profileId)
                    val drinkTypeFilters =
                        sharedFilterRepository.getDrinkTypeFiltersByProfileId(currentProfile.profileId)

                    calculateAvailableDrinks(matches, ingredientFilters, drinkTypeFilters)
                }

                _currentDrink.value = _availableDrinks.value.firstOrNull() ?: AppDrink()
                _currentDrink.value = drinkWrapper.getDrinkById(_currentDrink.value.drinkId)

                allDrinksMatched.value =
                    _availableDrinks.value.isEmpty() && bypassFilter.value == true
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

    private suspend fun calculateAvailableDrinks(
        matches: List<AppMatch>,
        ingredientFilters: List<AppIngredientValueFilter>,
        drinkTypeFilters: List<AppDrinkTypeFilter>
    ) {
        val allDrinks = drinkRepository.getAllDrinks().first()

        val drinkTypeFilterValues = drinkTypeFilters.map { it.drinkTypeFilterValue }
        val ingredientFilterValues = ingredientFilters.map { it.ingredientFilterValue }

        val availableDrinks = allDrinks.filter { drink ->
            val hasValidCategory =
                drinkTypeFilterValues.isEmpty() || drinkTypeFilterValues.contains(drink.categoryName)

            val hasMatchingIngredient =
                ingredientFilterValues.isEmpty() || drinkIngredientCrossRefRepository
                    .getIngredientsForDrink(drink.drinkId)
                    .any { ingredient -> ingredientFilterValues.contains(ingredient.ingredientName) }

            val isNotMatched = matches.none { it.drinkId == drink.drinkId }

            hasValidCategory && hasMatchingIngredient && isNotMatched
        }

        _availableDrinks.value = availableDrinks
    }

    fun setCurrentProfile(profile: AppProfile) = viewModelScope.launch {
        profileService.setCurrentProfile(profile)

        bypassFilter.value = false
        allDrinksMatched.value = false
        isInitialLoad.value = true
        evaluateCurrentDrink()
    }

    fun handleMatchingRequest(match: Boolean, drinkId: Int, profileId: Int) =
        viewModelScope.launch {
            matchService.insert(AppMatch(drinkId, profileId, match))
            evaluateCurrentDrink()
        }

    fun deleteProfile(profile: AppProfile) = viewModelScope.launch(Dispatchers.IO) {
        profileService.deleteProfile(profile)
        setCurrentProfile(profileService.allProfiles.first().first())
    }
}