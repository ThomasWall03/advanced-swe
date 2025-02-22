package de.bilkewall.plugins.database.drink

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drink_table")
data class DrinkEntity(
    @PrimaryKey(autoGenerate = false)
    val drinkId: Int = 0,
    val drinkName: String = "",
    val videoUrl: String = "",
    val categoryName: String = "",
    val alcoholic: String = "",
    val glassType: String = "",
    val instructionsEN: String = "",
    val instructionsDE: String = "",
    val thumbnailUrl: String = "",
    val dateModified: String = ""
)