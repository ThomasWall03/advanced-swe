package de.bilkewall.plugins.database.drinkIngredientCrossRef

import androidx.room.Entity

@Entity(tableName = "drink_ingredient_cross_ref", primaryKeys = ["drinkId", "ingredientName"])
data class DrinkIngredientCrossRef(
    val drinkId: Int,
    val ingredientName: String,
    val unit: String
)