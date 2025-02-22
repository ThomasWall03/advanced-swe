package de.bilkewall.adapters.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.application.service.DrinkService
import de.bilkewall.domain.Drink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DrinkListViewModel(
    private val drinkService: DrinkService
) : ViewModel() {
    var drinks: Flow<List<Drink>> = drinkService.getAllDrinks()
    var errorMessage: String by mutableStateOf("")
    var loading: Boolean by mutableStateOf(false)

    var drinkSearchText: String by mutableStateOf("")

    fun getAllDrinks() {
        viewModelScope.launch {
            errorMessage = ""
            loading = true

            try {
                drinks = drinkService.getAllDrinks()
            } catch (e: Exception) {
                errorMessage = e.message.toString()
                Log.e("DrinkListViewModel.getAllDrinks", "Error: ${e.message}")
            } finally {
                loading = false
            }
        }
    }

    fun getDrinksByName(name: String) {
        viewModelScope.launch {
            errorMessage = ""
            loading = true
            try {
                drinks = drinkService.getDrinksByName(name)
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            } finally {
                loading = false
            }
        }
    }
}