package de.bilkewall.adapters.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.DrinkIngredientWrapper
import de.bilkewall.domain.AppDrink
import de.bilkewall.domain.AppDrinkIngredientCrossRef
import kotlinx.coroutines.launch

class DrinkDetailViewModel(
    private var drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface,
    private var drinkIngredientWrapper: DrinkIngredientWrapper
) : ViewModel() {

    var drink: AppDrink by mutableStateOf(AppDrink())
    var ingredients: List<AppDrinkIngredientCrossRef> by mutableStateOf(listOf())

    private var errorMessage: String by mutableStateOf("")
    var loading: Boolean by mutableStateOf(false)

    fun setDrinkById(id: String) {
        viewModelScope.launch {
            loading = true
            try {
                ingredients = drinkIngredientCrossRefRepository.getIngredientsForDrink(id.toInt())
                drink = drinkIngredientWrapper.getDrinkById(id.toInt())
            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"
                Log.e("DrinkDetailViewModel.setDrinkById", errorMessage)
            } finally {
                loading = false
            }
        }
    }
}