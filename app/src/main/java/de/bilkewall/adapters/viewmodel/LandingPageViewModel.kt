package de.bilkewall.adapters.viewmodel

import de.bilkewall.adapters.DatabasePopulator
import de.bilkewall.application.service.DrinkFetchingService
import de.bilkewall.application.service.ProfileManagementService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LandingPageViewModel(
    private val drinkFetchingService: DrinkFetchingService,
    private val profileManagementService: ProfileManagementService,
    private val populator: DatabasePopulator,
) {
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    suspend fun populateDatabase() {
        val currentDrinkCount = drinkFetchingService.getDrinkCount()

        if (currentDrinkCount < 100) {
            // Clear existing data and populate new
            populator.clearExistingData()
            insertInitialData()
        } else {
            _isLoading.value = false
        }
    }

    private suspend fun insertInitialData() {
        _isLoading.value = true
        try {
            populator.insertInitialData()
        } catch (e: Exception) {
            _errorMessage.value = e.message.toString()
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun checkIfProfilesExist(): Boolean {
        return profileManagementService.checkIfProfilesExist()
    }
}
