package de.bilkewall.plugins.view.matches

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.bilkewall.adapters.viewmodel.MatchesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MatchesAndroidViewModel (
    val viewModel: MatchesViewModel
) : ViewModel() {
    var matchSearchText: String by mutableStateOf("")

    fun loadMatchedDrinks() = viewModelScope.launch(Dispatchers.IO) {
        viewModel.loadMatchedDrinks()
    }

    fun getMatchesByName(drinkSearchText: String) = viewModelScope.launch {
        viewModel.getMatchesByName(drinkSearchText)
    }
}