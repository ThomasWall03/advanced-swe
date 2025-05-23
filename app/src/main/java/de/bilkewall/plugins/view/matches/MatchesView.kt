package de.bilkewall.plugins.view.matches

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import de.bilkewall.cinder.R
import de.bilkewall.plugins.view.drinkList.DrinkList
import de.bilkewall.plugins.view.drinkList.SearchBar
import de.bilkewall.plugins.view.utils.CustomLoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesView(
    navController: NavController,
    matchesViewModel: MatchesAndroidViewModel,
    bottomBar: @Composable () -> Unit,
) {
    val adapter = matchesViewModel.viewModel
    val isLoading by adapter.isLoading.collectAsState()
    val errorMessage by adapter.errorMessage.collectAsState()

    val visibleDrinks by adapter.visibleDrinks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.matches)) })
        },
        bottomBar = bottomBar,
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LaunchedEffect(Unit) {
                matchesViewModel.loadMatchedDrinks()
            }

            SearchBar(
                searchBarText = matchesViewModel.matchSearchText,
                onTextChange = {
                    matchesViewModel.matchSearchText = it
                    if (it.isEmpty()) {
                        matchesViewModel.loadMatchedDrinks()
                    } else {
                        matchesViewModel.getMatchesByName(it)
                    }
                },
            )
            if (isLoading) {
                CustomLoadingIndicator()
            } else {
                DrinkList(
                    errorMessage,
                    visibleDrinks,
                    onClick = {
                        navController.navigate("cocktailDetailView/$it")
                    },
                    onRetryClick = {
                        matchesViewModel.getMatchesByName(matchesViewModel.matchSearchText)
                    },
                )
            }
        }
    }
}
