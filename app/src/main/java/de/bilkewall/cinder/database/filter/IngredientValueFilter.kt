package de.bilkewall.cinder.database.filter

import androidx.room.Entity

@Entity(tableName = "ingredient_value_filter_table", primaryKeys = ["ingredientFilterValue", "profileId"])
data class IngredientValueFilter (
    val ingredientFilterValue: String,
    val profileId: Int
)