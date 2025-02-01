package de.bilkewall.cinder.view.drinkList

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.cinder.database.drink.Drink
import de.bilkewall.cinder.service.DrinkService
import de.bilkewall.cinder.util.toDrinkAndRelations
import kotlinx.coroutines.launch

class DrinkListViewModel : ViewModel() {

    private val drinkService = DrinkService()

    var drinks: List<Drink> by mutableStateOf(listOf())
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
                val allDrinks =
                    drinkService.getAllDrinksAtoZ().map { it.toDrinkAndRelations().first }
                drinks = allDrinks
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
                val drinksByName = drinkService.getDrinksByName(name)
                drinks = drinksByName.map { it.toDrinkAndRelations().first }
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            } finally {
                loading = false
            }

        }
    }
}