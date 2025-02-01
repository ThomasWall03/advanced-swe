package de.bilkewall.plugins.view.drinkList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.bilkewall.cinder.R
import de.bilkewall.plugins.database.drink.Drink
import de.bilkewall.plugins.view.drinkList.cell.DrinkCell
import de.bilkewall.plugins.view.utils.CustomLoadingIndicator
import de.bilkewall.plugins.view.utils.ErrorCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinkListView(
    navController: NavController,
    drinkListViewModel: DrinkListViewModel,
    bottomBar: @Composable () -> Unit
) {
    LaunchedEffect(Unit) {
        drinkListViewModel.fillListIfEmpty()
    }


    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.all_drinks)) })
        },
        bottomBar = bottomBar
    ) { innerPadding ->


        Column(modifier = Modifier.padding(innerPadding)) {
            SearchBar(
                searchBarText = drinkListViewModel.drinkSearchText,
                onTextChange = {
                    drinkListViewModel.drinkSearchText = it
                    if (drinkListViewModel.drinkSearchText.isEmpty()) {
                        drinkListViewModel.getAllDrinks()
                    } else {
                        drinkListViewModel.getDrinksByName(drinkListViewModel.drinkSearchText)
                    }
                })
            if (drinkListViewModel.loading) {
                CustomLoadingIndicator()
            } else {
                DrinkList(
                    drinkListViewModel.errorMessage,
                    drinkListViewModel.drinks,
                    onClick = {
                        navController.navigate("cocktailDetailView/$it")
                    },
                    onRetryClick = {
                        drinkListViewModel.getDrinksByName(drinkListViewModel.drinkSearchText)
                    }
                )
            }
        }

    }
}

@Composable
fun SearchBar(searchBarText: String, onTextChange: (String) -> (Unit)) {

    TextField(
        value = searchBarText,
        onValueChange = {
            onTextChange(it)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(R.string.search)
            )
        },
        placeholder = { Text(stringResource(R.string.search_for_drink)) },
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun DrinkList(
    errorMessage: String,
    drinks: List<Drink>,
    onClick: (String) -> (Unit),
    onRetryClick: () -> Unit
) {
    if (errorMessage.isEmpty()) {
        if (drinks.isEmpty()) {
            ErrorCard(
                errorHeading = stringResource(R.string.no_drinks_found),
                errorInformation = stringResource(R.string.no_drinks_found_information)
            )
        }
        LazyColumn {
            itemsIndexed(items = drinks) { _, item ->
                DrinkCell(drink = item, onClick)
            }
        }
    } else {
        ErrorCard(
            errorHeading = stringResource(R.string.generic_error_title),
            errorInformation = errorMessage,
            bottomComposable = {
                Button(
                    onClick = onRetryClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = stringResource(R.string.retry))
                }
            }
        )
    }
}