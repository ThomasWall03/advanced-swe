package de.bilkewall.adapters.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.adapters.repository.DrinkRepositoryInterface
import de.bilkewall.domain.AppDrink
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class DrinkListViewModel(
    private val drinkRepository: DrinkRepositoryInterface
) : ViewModel() {

    var drinks: List<AppDrink> by mutableStateOf(listOf())
    var errorMessage: String by mutableStateOf("")
    var loading: Boolean by mutableStateOf(false)

    var drinkSearchText: String by mutableStateOf("")

    fun fillListIfEmpty() {
        if (drinks.isEmpty()) {
            this.getAllDrinks()
        }
    }

    fun getAllDrinks() {
        viewModelScope.launch {
            errorMessage = ""
            loading = true

            try {
                val allDrinks = drinkRepository.getAllDrinks()
                drinks = allDrinks.toList().flatten()
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
                val drinksByName = drinkRepository.getDrinksByName(name)
                drinks = drinksByName
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            } finally {
                loading = false
            }

        }
    }
}