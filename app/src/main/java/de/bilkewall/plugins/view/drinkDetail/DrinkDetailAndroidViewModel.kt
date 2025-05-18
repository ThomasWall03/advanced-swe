package de.bilkewall.plugins.view.drinkDetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.adapters.viewmodel.DrinkDetailViewModel
import de.bilkewall.domain.Drink
import kotlinx.coroutines.launch

class DrinkDetailAndroidViewModel(
    val viewModel: DrinkDetailViewModel
) : ViewModel() {
    var drink: Drink by mutableStateOf(Drink())

    fun setDrinkById(id: String) = viewModelScope.launch {
        drink = viewModel.setDrinkById(id)
    }
}