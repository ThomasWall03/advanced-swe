package de.bilkewall.adapters.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.adapters.service.DrinkService
import de.bilkewall.main.di.DependencyProvider
import de.bilkewall.plugins.database.filter.DrinkTypeFilter
import de.bilkewall.plugins.database.filter.IngredientValueFilter
import de.bilkewall.plugins.database.profile.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateProfileViewModel: ViewModel() {
    private val profileRepository = DependencyProvider.profileRepository
    private val sharedFilterRepository = DependencyProvider.sharedFilterRepository
    private val drinkIngredientCrossRefRepository = DependencyProvider.drinkIngredientCrossRefRepository

    private val _drinkTypeFilterValues = MutableStateFlow<List<String>>(emptyList())
    val drinkTypeFilterValues: StateFlow<List<String>> = _drinkTypeFilterValues

    private val _selectedDrinkTypeOptions = MutableStateFlow<List<String>>(emptyList())
    val selectedDrinkTypeOptions: StateFlow<List<String>> get() = _selectedDrinkTypeOptions
    private val _selectedIngredientOptions = MutableStateFlow<List<String>>(emptyList())
    val selectedIngredientOptions: StateFlow<List<String>> get() = _selectedIngredientOptions

    var allIngredients: Flow<List<String>> = drinkIngredientCrossRefRepository.allIngredients

    var errorMessage: String by mutableStateOf("")
    private var loading: Boolean by mutableStateOf(false)

    private val drinkService = DrinkService()

    fun saveProfile(profileName: String) = viewModelScope.launch {
        profileRepository.deactivateActiveProfile()
        val id = profileRepository.insert(Profile(profileName = profileName, isActiveProfile = true))

        _selectedDrinkTypeOptions.value.forEach { filter ->
            sharedFilterRepository.insertDrinkTypeFilter(DrinkTypeFilter(filter, id.toInt()))
        }

        _selectedIngredientOptions.value.forEach { filter ->
            sharedFilterRepository.insertIngredientValueFilter(IngredientValueFilter(filter, id.toInt()))
        }
    }

    fun fetchDrinkTypeFilterValues() {
        viewModelScope.launch {
            errorMessage = ""
            loading = true
            try {
                val values = drinkService.getDrinkCategories().map { it.strCategory }
                _drinkTypeFilterValues.value = values
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            } finally {
                loading = false
            }
        }
    }

    fun updateSelectedOptions(filterType: Int, options: List<String>) {
        when (filterType) {
            0 -> {
                _selectedDrinkTypeOptions.value = options
            }
            1 -> {
                _selectedIngredientOptions.value = options
            }
        }
    }

    fun clearSelectedOptions() {
        _selectedIngredientOptions.value = emptyList()
        _selectedDrinkTypeOptions.value = emptyList()
    }
}