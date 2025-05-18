package de.bilkewall.plugins.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.adapters.viewmodel.MainViewModel
import de.bilkewall.domain.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainAndroidViewModel(
    val viewModel: MainViewModel
) : ViewModel() {
    fun evaluateCurrentDrink() = viewModelScope.launch(Dispatchers.IO) {
        viewModel.evaluateCurrentDrink()
    }

    fun toggleFilterBypass(byPassFilter: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        viewModel.toggleFilterBypass(byPassFilter)
        viewModel.evaluateCurrentDrink()
    }

    fun setCurrentProfile(profile: Profile) = viewModelScope.launch(Dispatchers.IO) {
        viewModel.setCurrentProfile(profile)
    }

    fun handleMatchingRequest(match: Boolean, drinkId: Int, profileId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            viewModel.handleMatchingRequest(match, drinkId, profileId)
        }

    fun deleteProfile(profile: Profile) = viewModelScope.launch(Dispatchers.IO) {
        viewModel.deleteProfile(profile)
    }

}