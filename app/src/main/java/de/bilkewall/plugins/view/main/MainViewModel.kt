package de.bilkewall.plugins.view.main

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.domain.AppDrinkDto
import de.bilkewall.main.di.DependencyProvider
import de.bilkewall.plugins.database.filter.DrinkTypeFilter
import de.bilkewall.plugins.database.filter.IngredientValueFilter
import de.bilkewall.plugins.database.match.Match
import de.bilkewall.plugins.database.profile.Profile
import de.bilkewall.plugins.util.toAppDrinkDto
import de.bilkewall.plugins.util.toDrinkAndRelations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val sharedFilterRepository = DependencyProvider.sharedFilterRepository
    private val profileRepository = DependencyProvider.profileRepository
    private val matchRepository = DependencyProvider.matchRepository
    private val drinkRepository = DependencyProvider.drinkRepository
    private val drinkIngredientCrossRefRepository =
        DependencyProvider.drinkIngredientCrossRefRepository
    private val drinkService = DependencyProvider.drinkService

    val allProfiles: Flow<List<Profile>> = profileRepository.allProfiles
    val currentProfile = profileRepository.activeProfile

    private val _availableDrinks = MutableStateFlow<List<AppDrinkDto>>(emptyList())
    val availableDrinks: StateFlow<List<AppDrinkDto>> = _availableDrinks

    private val _currentDrink = MutableStateFlow(AppDrinkDto())
    val currentDrink: StateFlow<AppDrinkDto> get() = _currentDrink

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

                _currentDrink.value = _availableDrinks.value.firstOrNull() ?: AppDrinkDto()

                allDrinksMatched.value =
                    _availableDrinks.value.isEmpty() && bypassFilter.value == true

                if (_currentDrink.value.drinkId != 0) {
                    updateDataBaseEntry(_currentDrink.value)
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

    private suspend fun calculateAvailableDrinks(
        matches: List<Match>,
        ingredientFilters: List<IngredientValueFilter>,
        drinkTypeFilters: List<DrinkTypeFilter>
    ) {
        val allDrinks = drinkRepository.allDrinks.first()

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

        _availableDrinks.value = availableDrinks.map { drink ->
            val ingredients =
                drinkIngredientCrossRefRepository.getIngredientsForDrink(drink.drinkId)
            drink.toAppDrinkDto(
                ingredients.map { it.ingredientName },
                ingredients.map { it.unit },
            )
        }
    }

    private fun updateDataBaseEntry(drink: AppDrinkDto) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val apiDrink = drinkService.getDrinkById(drink.drinkId)[0]
            if (apiDrink.idDrink.toInt() != 0) {
                if (drink.dateModified != apiDrink.dateModified) {
                    val (updatedDrink, relations) = apiDrink.toDrinkAndRelations()
                    drinkRepository.update(updatedDrink)

                    drinkIngredientCrossRefRepository.deleteAllRelationsOfADrink(drink.drinkId)
                    relations.forEach { relation ->
                        drinkIngredientCrossRefRepository.insert(relation)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MainViewModel.updateDataBaseEntry", "Error: ${e.message}")
        }
    }

    fun setCurrentProfile(profile: Profile) = viewModelScope.launch {
        profileRepository.deactivateActiveProfile()
        profileRepository.setActiveProfile(profile.profileId)
        bypassFilter.value = false
        allDrinksMatched.value = false
        isInitialLoad.value = true
        evaluateCurrentDrink()
    }

    fun handleMatchingRequest(match: Boolean, drinkId: Int, profileId: Int) =
        viewModelScope.launch {
            matchRepository.insert(Match(drinkId, profileId, match))
            evaluateCurrentDrink()
        }

    fun deleteProfile(profile: Profile) = viewModelScope.launch(Dispatchers.IO) {
        profileRepository.delete(profile)
        sharedFilterRepository.deleteIngredientValueFiltersByProfileId(profile.profileId)
        sharedFilterRepository.deleteDrinkTypeFiltersByProfileId(profile.profileId)
        matchRepository.deleteMatchesForProfile(profile.profileId)
    }
}