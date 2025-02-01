package de.bilkewall.cinder.view.landingPage

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.cinder.di.DependencyProvider
import de.bilkewall.cinder.di.DependencyProvider.drinkService
import de.bilkewall.cinder.util.toDrinkAndRelations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LandingPageViewModel : ViewModel() {
    private val drinkRepository = DependencyProvider.drinkRepository
    private val drinkIngredientCrossRefRepository =
        DependencyProvider.drinkIngredientCrossRefRepository
    private val profileRepository = DependencyProvider.profileRepository

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _profilesExist = MutableStateFlow(false)
    val profilesExist: StateFlow<Boolean> = _profilesExist

    private val allDrinks = drinkRepository.allDrinks
    private var errorMessage: String by mutableStateOf("")

    init {
        populateDatabase()
    }

    private fun populateDatabase() = viewModelScope.launch {
        val currentDrinkCount = drinkRepository.getDrinkCount()

        if (currentDrinkCount < 100) {
            Log.d("DEBUG", "Insufficient drinks in the database. Populating data...")
            // Clear existing data and populate anew
            drinkRepository.deleteAllDrinks()
            drinkIngredientCrossRefRepository.deleteAllRelations()
            insertInitialData()
        } else {
            _isLoading.value = false
        }
    }

    private fun insertInitialData() = viewModelScope.launch {
        _isLoading.value = true
        try {
            val allDrinksAPI = drinkService.getAllDrinksAtoZ()
            allDrinksAPI.forEach { drink ->
                val (drinkType, relations) = drink.toDrinkAndRelations()
                if (allDrinks.first().map { it.drinkId }.contains(drinkType.drinkId)) {
                    drinkRepository.update(drinkType)
                    drinkIngredientCrossRefRepository.deleteAllRelationsOfADrink(drinkType.drinkId)
                } else
                    drinkRepository.insert(
                        drinkType
                    )

                var relationInsertionIndex = 0
                relations.forEach { relation ->
                    if (!relations.subList(0, relationInsertionIndex).map { it.ingredientName }
                            .contains(relation.ingredientName)) {
                        drinkIngredientCrossRefRepository.insert(relation)
                    }
                    relationInsertionIndex++
                }
            }
        } catch (e: Exception) {
            Log.e("LandingPageViewModel.insertInitialData", e.message.toString())
            errorMessage = e.message.toString()
        } finally {
            _isLoading.value = false
        }
    }

    fun checkIfProfilesExist() = viewModelScope.launch {
        profileRepository.getProfileCount().let {
            _profilesExist.value = it > 0
        }
    }

    fun deleteAllData() = viewModelScope.launch {
        drinkRepository.deleteAllDrinks()
        drinkIngredientCrossRefRepository.deleteAllRelations()
    }
}