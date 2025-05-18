package de.bilkewall.plugins.view.drinkList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.adapters.viewmodel.DrinkListViewModel
import kotlinx.coroutines.launch

class DrinkListAndroidViewModel (
    val viewModel: DrinkListViewModel
) : ViewModel(){
    var drinkSearchText: String by mutableStateOf("")

    fun getAllDrinks() = viewModelScope.launch {
        viewModel.getAllDrinks()
    }

    fun getDrinksByName(name: String) = viewModelScope.launch {
        viewModel.getDrinksByName(name)
    }
}