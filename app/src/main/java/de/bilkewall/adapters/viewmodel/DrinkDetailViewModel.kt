package de.bilkewall.adapters.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.application.service.DrinkService
import de.bilkewall.domain.AppDrink
import kotlinx.coroutines.launch

class DrinkDetailViewModel(
    private var drinkService: DrinkService
) : ViewModel() {
    var drink: AppDrink by mutableStateOf(AppDrink())
    private var errorMessage: String by mutableStateOf("")
    var loading: Boolean by mutableStateOf(false)

    fun setDrinkById(id: String) {
        viewModelScope.launch {
            loading = true
            try {
                drink = drinkService.getDrinkById(id.toInt())
            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"
                Log.e("DrinkDetailViewModel.setDrinkById", errorMessage)
            } finally {
                loading = false
            }
        }
    }
}