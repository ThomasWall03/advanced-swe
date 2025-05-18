package de.bilkewall.adapters.viewmodel

import de.bilkewall.application.service.DrinkFetchingService
import de.bilkewall.domain.Drink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DrinkDetailViewModel(
    private var drinkFetchingService: DrinkFetchingService,
) {
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    suspend fun setDrinkById(id: String): Drink {
        _isLoading.value = true
        try {
            return drinkFetchingService.getDrinkById(id.toInt())
        } catch (e: Exception) {
            _errorMessage.value = e.message.toString()
            return Drink()
        } finally {
            _isLoading.value = false
        }
    }
}
