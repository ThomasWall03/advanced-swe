package de.bilkewall.plugins.database.filter

import androidx.room.Entity

@Entity(tableName = "drink_type_filter_table", primaryKeys = ["drinkTypeFilterValue", "profileId"])
data class DrinkTypeFilterEntity(
    val drinkTypeFilterValue: String,
    val profileId: Int,
)
