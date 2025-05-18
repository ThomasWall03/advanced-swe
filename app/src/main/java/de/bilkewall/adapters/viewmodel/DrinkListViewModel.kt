package de.bilkewall.adapters.viewmodel

import de.bilkewall.application.service.DrinkFetchingService
import de.bilkewall.domain.Drink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DrinkListViewModel(
    private val drinkFetchingService: DrinkFetchingService,
) {
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    var drinks: Flow<List<Drink>> = drinkFetchingService.getAllDrinks()

    fun getAllDrinks() {
        _errorMessage.value = ""
        _isLoading.value = true

        try {
            drinks = drinkFetchingService.getAllDrinks()
        } catch (e: Exception) {
            _errorMessage.value = e.message.toString()
        } finally {
            _isLoading.value = false
        }
    }

    fun getDrinksByName(name: String) {
        _errorMessage.value = ""
        _isLoading.value = true

        try {
            drinks = drinkFetchingService.getDrinksByName(name)
        } catch (e: Exception) {
            _errorMessage.value = e.message.toString()
        } finally {
            _isLoading.value = false
        }
    }
}
