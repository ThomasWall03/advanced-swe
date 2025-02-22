package de.bilkewall.plugins.database.filter

import androidx.room.Entity

@Entity(
    tableName = "ingredient_value_filter_table",
    primaryKeys = ["ingredientFilterValue", "profileId"]
)
data class IngredientValueFilterEntity(
    val ingredientFilterValue: String,
    val profileId: Int
)