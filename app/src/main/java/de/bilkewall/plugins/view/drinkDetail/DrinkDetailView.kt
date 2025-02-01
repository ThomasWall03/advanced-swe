package de.bilkewall.plugins.view.drinkDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import de.bilkewall.adapters.viewmodel.DrinkDetailViewModel
import de.bilkewall.cinder.R
import de.bilkewall.domain.AppDrink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinkDetailView(
    navController: NavHostController,
    drinkDetailViewModel: DrinkDetailViewModel,
    drinkId: String
) {
    LaunchedEffect(Unit) {
        drinkDetailViewModel.setDrinkById(drinkId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Drink Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        DetailViewCard(innerPadding, drinkDetailViewModel.drink, drinkDetailViewModel.loading)
    }
}

@Composable
fun DetailViewCard(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    drink: AppDrink,
    loading: Boolean = false,
    bottomSpacer: Dp = 0.dp
) {
    if (loading) {
        CircularProgressIndicator()
    } else {
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            item { ImageAndName(drink) }
            item { Ingredients(drink) }
            item { Instructions(drink) }

            item {
                Spacer(modifier = Modifier.height(bottomSpacer)) // Adjust the height as needed
            }
        }
    }
}

@Composable
private fun DetailPartCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            content()
        }
    }
}

@Composable
private fun ImageAndName(drink: AppDrink) {
    DetailPartCard {
        Column {
            AsyncImage(
                model = drink.thumbnailUrl,
                contentDescription = drink.drinkName,
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop
            )

            Text(
                drink.drinkName,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 24.sp),
            )
        }
    }

}

@Composable
private fun Ingredients(drink: AppDrink) {
    DetailPartCard {
        Text(
            stringResource(R.string.ingredients),
            fontWeight = FontWeight.Bold,
        )

        Column {
            for (i in drink.ingredients.indices) {
                Text(
                    "${drink.measurements[i]} ${drink.ingredients[i]}",
                )
            }
        }
    }
}


@Composable
private fun Instructions(drink: AppDrink) {
    DetailPartCard {
        Text(
            stringResource(R.string.instructions),
            fontWeight = FontWeight.Bold,
        )
        Text(
            drink.instructionsEN,
        )
    }
}

