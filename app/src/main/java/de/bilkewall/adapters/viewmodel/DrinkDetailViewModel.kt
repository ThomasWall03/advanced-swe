package de.bilkewall.adapters.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.domain.AppDrink
import de.bilkewall.main.di.DependencyProvider
import de.bilkewall.plugins.database.drinkIngredientCrossRef.DrinkIngredientCrossRef
import de.bilkewall.plugins.util.toAppDrinkDto
import kotlinx.coroutines.launch

class DrinkDetailViewModel : ViewModel() {
    private val drinkIngredientCrossRefRepository = DependencyProvider.drinkIngredientCrossRefRepository
    private val drinkRepository = DependencyProvider.drinkRepository

    var drink: AppDrink by mutableStateOf(AppDrink())
    var ingredients: List<DrinkIngredientCrossRef> by mutableStateOf(listOf())

    private var errorMessage: String by mutableStateOf("")
    var loading: Boolean by mutableStateOf(false)

    fun setDrinkById(id: String) {
        viewModelScope.launch {
            loading = true
            try {
                ingredients = drinkIngredientCrossRefRepository.getIngredientsForDrink(id.toInt())
                drink = drinkRepository.getDrinkById(id.toInt()).toAppDrinkDto(
                    ingredients.map { it.ingredientName },
                    ingredients.map { it.unit }
                )
            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"
                Log.e("DrinkDetailViewModel.setDrinkById", errorMessage)
            } finally {
                loading = false
            }
        }
    }
}