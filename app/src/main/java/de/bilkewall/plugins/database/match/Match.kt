package de.bilkewall.plugins.database.match

import androidx.room.Entity

@Entity(tableName = "match_table", primaryKeys = ["drinkId", "profileId"])
data class Match(
    val drinkId: Int,
    val profileId: Int,
    val outcome: Boolean
)