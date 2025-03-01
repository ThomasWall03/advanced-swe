package de.bilkewall.plugins.database.filter

import androidx.room.Entity

@Entity(
    tableName = "ingredient_value_filter_table",
    primaryKeys = ["ingredientFilterValue", "profileId"],
)
data class IngredientFilterEntity(
    val ingredientFilterValue: String,
    val profileId: Int,
)
