package de.bilkewall.cinder.database.filter

import androidx.room.Entity

@Entity(tableName = "drink_type_filter_table", primaryKeys = ["drinkTypeFilterValue", "profileId"])
data class DrinkTypeFilter(
    val drinkTypeFilterValue: String,
    val profileId: Int
)
