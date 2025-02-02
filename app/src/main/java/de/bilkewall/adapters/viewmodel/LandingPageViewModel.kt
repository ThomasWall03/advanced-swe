package de.bilkewall.adapters.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.application.service.api.ApiService
import de.bilkewall.domain.AppDrinkIngredientCrossRef
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LandingPageViewModel(
    private var drinkRepository: DrinkRepositoryInterface,
    private var apiService: ApiService,
    private var drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface,
    private var profileRepository: ProfileRepositoryInterface
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _profilesExist = MutableStateFlow(false)
    val profilesExist: StateFlow<Boolean> = _profilesExist

    private val allDrinks = drinkRepository.getAllDrinks()
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
            val allDrinksAPI = apiService.getAllDrinksAtoZ()
            allDrinksAPI.forEach { drink ->
                if (allDrinks.first().map { it.drinkId }.contains(drink.drinkId)) {
                    drinkRepository.update(drink)
                    drinkIngredientCrossRefRepository.deleteAllRelationsOfADrink(drink.drinkId)
                } else {
                    drinkRepository.insert(
                        drink
                    )
                }

                var relationInsertionIndex = 0
                drink.ingredients.forEach { ingredient ->
                    if (!drink.ingredients.subList(0, relationInsertionIndex).map { ingredient }
                            .contains(ingredient)) {
                        drinkIngredientCrossRefRepository.insert(
                            AppDrinkIngredientCrossRef(
                                drink.drinkId,
                                ingredient,
                                drink.measurements[relationInsertionIndex]
                            )
                        )
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