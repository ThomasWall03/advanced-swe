package de.bilkewall.plugins.view.createProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.adapters.viewmodel.CreateProfileViewModel
import kotlinx.coroutines.launch

class CreateProfileAndroidViewModel(
    val createProfileViewModel: CreateProfileViewModel
) : ViewModel() {
    fun saveProfile(profileName: String) = viewModelScope.launch {
        createProfileViewModel.saveProfile(profileName)
    }

    fun fetchDrinkTypeFilterValues() = viewModelScope.launch {
        createProfileViewModel.fetchDrinkTypeFilterValues()
    }

    fun updateIngredientFilterValues(values: List<String>) {
       createProfileViewModel.updateIngredientFilterValues(values)
    }

    fun updateDrinkTypeFilterValues(values: List<String>) {
        createProfileViewModel.updateDrinkTypeFilterValues(values)
    }

    fun clearSelectedOptions() {
        createProfileViewModel.clearSelectedOptions()
    }
}