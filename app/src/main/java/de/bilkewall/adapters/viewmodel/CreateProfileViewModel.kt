package de.bilkewall.adapters.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.application.repository.SharedFilterRepositoryInterface
import de.bilkewall.application.service.api.DrinkService
import de.bilkewall.domain.AppDrinkTypeFilter
import de.bilkewall.domain.AppIngredientValueFilter
import de.bilkewall.domain.AppProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateProfileViewModel(
    private var profileRepository: ProfileRepositoryInterface,
    private var sharedFilterRepository: SharedFilterRepositoryInterface,
    private var drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface,
    private var drinkService: DrinkService
) : ViewModel() {
    private val _drinkTypeFilterValues = MutableStateFlow<List<String>>(emptyList())
    val drinkTypeFilterValues: StateFlow<List<String>> = _drinkTypeFilterValues

    private val _selectedDrinkTypeOptions = MutableStateFlow<List<String>>(emptyList())
    val selectedDrinkTypeOptions: StateFlow<List<String>> get() = _selectedDrinkTypeOptions
    private val _selectedIngredientOptions = MutableStateFlow<List<String>>(emptyList())
    val selectedIngredientOptions: StateFlow<List<String>> get() = _selectedIngredientOptions

    var allIngredients: Flow<List<String>> =
        drinkIngredientCrossRefRepository.getAllIngredientsSortedByName()

    var errorMessage: String by mutableStateOf("")
    private var loading: Boolean by mutableStateOf(false)

    fun saveProfile(profileName: String) = viewModelScope.launch {
        profileRepository.deactivateActiveProfile()
        val id =
            profileRepository.insert(AppProfile(profileName = profileName, isActiveProfile = true))

        _selectedDrinkTypeOptions.value.forEach { filter ->
            sharedFilterRepository.insertDrinkTypeFilter(AppDrinkTypeFilter(filter, id.toInt()))
        }

        _selectedIngredientOptions.value.forEach { filter ->
            sharedFilterRepository.insertIngredientValueFilter(
                AppIngredientValueFilter(
                    filter,
                    id.toInt()
                )
            )
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

    fun updateIngredientFilterValues(values: List<String>) {
        _selectedIngredientOptions.value = values
    }

    fun updateDrinkTypeFilterValues(values: List<String>) {
        _drinkTypeFilterValues.value = values
    }

    fun clearSelectedOptions() {
        _selectedIngredientOptions.value = emptyList()
        _selectedDrinkTypeOptions.value = emptyList()
    }
}