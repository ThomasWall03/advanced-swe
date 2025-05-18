package de.bilkewall.adapters.viewmodel

import de.bilkewall.application.service.CategoryService
import de.bilkewall.application.service.IngredientService
import de.bilkewall.application.service.ProfileManagementService
import de.bilkewall.application.service.SharedFilterService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CreateProfileViewModel(
    private val profileManagementService: ProfileManagementService,
    private val sharedFilterService: SharedFilterService,
    private val ingredientService: IngredientService,
    private val categoryService: CategoryService,
)  {
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _drinkTypeFilterValues = MutableStateFlow<List<String>>(emptyList())
    val drinkTypeFilterValues: StateFlow<List<String>> = _drinkTypeFilterValues

    private val _selectedDrinkTypeOptions = MutableStateFlow<List<String>>(emptyList())
    val selectedDrinkTypeOptions: StateFlow<List<String>> get() = _selectedDrinkTypeOptions
    private val _selectedIngredientOptions = MutableStateFlow<List<String>>(emptyList())
    val selectedIngredientOptions: StateFlow<List<String>> get() = _selectedIngredientOptions

    var allIngredients: Flow<List<String>> =
        ingredientService.getAllIngredientsSortedByName()

    suspend fun saveProfile(profileName: String) {
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

    suspend fun fetchDrinkTypeFilterValues() {
        _errorMessage.value = ""
        _isLoading.value = true
        try {
            val values =
                categoryService.getAllCategories().map { categories ->
                    categories.map { it.strCategory }
                }
            _drinkTypeFilterValues.value = values.first()
        } catch (e: Exception) {
            _errorMessage.value = e.message.toString()
        } finally {
            _isLoading.value = false
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
