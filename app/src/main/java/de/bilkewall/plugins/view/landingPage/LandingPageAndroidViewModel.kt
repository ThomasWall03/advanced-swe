package de.bilkewall.plugins.view.landingPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.adapters.viewmodel.LandingPageViewModel
import kotlinx.coroutines.launch

class LandingPageAndroidViewModel (
    val viewModel: LandingPageViewModel
): ViewModel() {
    var navigationRoute = "mainView"

    init {
        populateDatabase()
    }

    private fun populateDatabase() = viewModelScope.launch {
        viewModel.populateDatabase()
    }

    fun checkIfProfilesExist() = viewModelScope.launch {
        if(!viewModel.checkIfProfilesExist()) {
            navigationRoute = "createProfileView"
        }
    }
}