package de.bilkewall.plugins.database.category

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey(autoGenerate = false)
    val strCategory: String = "",
)