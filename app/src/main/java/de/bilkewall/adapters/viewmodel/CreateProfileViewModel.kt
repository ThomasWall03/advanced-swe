package de.bilkewall.adapters.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.application.service.api.ApiService
import de.bilkewall.application.service.database.CreateProfileService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateProfileViewModel(
    private val apiService: ApiService,
    private val createProfileService: CreateProfileService
) : ViewModel() {
    private val _drinkTypeFilterValues = MutableStateFlow<List<String>>(emptyList())
    val drinkTypeFilterValues: StateFlow<List<String>> = _drinkTypeFilterValues

    private val _selectedDrinkTypeOptions = MutableStateFlow<List<String>>(emptyList())
    val selectedDrinkTypeOptions: StateFlow<List<String>> get() = _selectedDrinkTypeOptions
    private val _selectedIngredientOptions = MutableStateFlow<List<String>>(emptyList())
    val selectedIngredientOptions: StateFlow<List<String>> get() = _selectedIngredientOptions

    var allIngredients: Flow<List<String>> =
        createProfileService.getAllIngredientsSortedByName()

    var errorMessage: String by mutableStateOf("")
    private var loading: Boolean by mutableStateOf(false)

    fun saveProfile(profileName: String) = viewModelScope.launch {
        createProfileService.saveProfile(
            profileName,
            _selectedDrinkTypeOptions.value,
            _selectedIngredientOptions.value
        )
    }

    fun fetchDrinkTypeFilterValues() {
        viewModelScope.launch {
            errorMessage = ""
            loading = true
            try {
                val values = apiService.getDrinkCategories().map { it.strCategory }
                _drinkTypeFilterValues.value = values
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            } finally {
                loading = false
            }
        }
    }

    fun updateIngredientFilterValues(values: List<String>) {
        _selectedIngredientOptions.value = values
    }

    fun updateDrinkTypeFilterValues(values: List<String>) {
        _selectedDrinkTypeOptions.value = values
    }

    fun clearSelectedOptions() {
        _selectedIngredientOptions.value = emptyList()
        _selectedDrinkTypeOptions.value = emptyList()
    }
}