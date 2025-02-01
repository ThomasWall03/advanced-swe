package de.bilkewall.plugins.view.drinkList.cell

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import de.bilkewall.domain.AppDrink
import de.bilkewall.plugins.database.drink.Drink
import sv.lib.squircleshape.SquircleShape

@Composable
fun DrinkCell(drink: AppDrink, onClick: (String) -> (Unit)) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(2f)
            .clickable { onClick(drink.drinkId.toString()) },
        shape = SquircleShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row {
            AsyncImage(
                model = drink.thumbnailUrl,
                contentDescription = drink.drinkName,
                modifier = Modifier
                    .weight(0.5f)
                    .aspectRatio(1f)
                    .padding(8.dp)
                    .clip(SquircleShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.5f)
            ) {
                Text(
                    drink.drinkName,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(fontSize = 24.sp),
                )
                Text(drink.categoryName)
            }
        }
    }
}