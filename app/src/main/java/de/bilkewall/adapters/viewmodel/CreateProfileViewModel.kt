package de.bilkewall.adapters.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.application.service.CategoryService
import de.bilkewall.application.service.IngredientService
import de.bilkewall.application.service.ProfileManagementService
import de.bilkewall.application.service.SharedFilterService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CreateProfileViewModel(
    private val profileManagementService: ProfileManagementService,
    private val sharedFilterService: SharedFilterService,
    private val ingredientService: IngredientService,
    private val categoryService: CategoryService,
) : ViewModel() {
    private val _drinkTypeFilterValues = MutableStateFlow<List<String>>(emptyList())
    val drinkTypeFilterValues: StateFlow<List<String>> = _drinkTypeFilterValues

    private val _selectedDrinkTypeOptions = MutableStateFlow<List<String>>(emptyList())
    val selectedDrinkTypeOptions: StateFlow<List<String>> get() = _selectedDrinkTypeOptions
    private val _selectedIngredientOptions = MutableStateFlow<List<String>>(emptyList())
    val selectedIngredientOptions: StateFlow<List<String>> get() = _selectedIngredientOptions

    var allIngredients: Flow<List<String>> =
        ingredientService.getAllIngredientsSortedByName()

    var errorMessage: String by mutableStateOf("")
    private var loading: Boolean by mutableStateOf(false)

    fun saveProfile(profileName: String) =
        viewModelScope.launch {
            val id =
                profileManagementService.saveProfile(
                    profileName,
                )
            sharedFilterService.saveFiltersForProfile(
                id,
                _selectedDrinkTypeOptions.value,
                _selectedIngredientOptions.value,
            )
        }

    fun fetchDrinkTypeFilterValues() {
        viewModelScope.launch {
            errorMessage = ""
            loading = true
            try {
                val values =
                    categoryService.getAllCategories().map { categories ->
                        categories.map { it.strCategory }
                    }
                _drinkTypeFilterValues.value = values.first()
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
