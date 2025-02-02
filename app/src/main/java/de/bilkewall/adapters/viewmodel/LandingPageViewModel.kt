package de.bilkewall.adapters.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.application.service.api.ApiService
import de.bilkewall.application.service.database.LandingPageService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LandingPageViewModel(
    private val apiService: ApiService,
    private val landingPageService: LandingPageService
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _profilesExist = MutableStateFlow(false)
    val profilesExist: StateFlow<Boolean> = _profilesExist

    private var errorMessage: String by mutableStateOf("")

    init {
        populateDatabase()
    }

    private fun populateDatabase() = viewModelScope.launch {
        val currentDrinkCount = landingPageService.getDrinkCount()

        if (currentDrinkCount < 100) {
            Log.d("DEBUG", "Insufficient drinks in the database. Populating data...")
            // Clear existing data and populate new
            landingPageService.clearExistingData()
            insertInitialData()
        } else {
            _isLoading.value = false
        }
    }

    private fun insertInitialData() = viewModelScope.launch {
        _isLoading.value = true
        try {
            val allDrinksAPI = apiService.getAllDrinksAtoZ()
            landingPageService.insertInitialData(allDrinksAPI)
        } catch (e: Exception) {
            Log.e("LandingPageViewModel.insertInitialData", e.message.toString())
            errorMessage = e.message.toString()
        } finally {
            _isLoading.value = false
        }
    }

    fun checkIfProfilesExist() = viewModelScope.launch {
        _profilesExist.value = landingPageService.checkIfProfilesExist()
    }
}