package de.bilkewall.plugins.database.filter

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DrinkTypeFilterDao {
    @Insert
    suspend fun insertDrinkTypeFilter(drinkTypeFilter: DrinkTypeFilterEntity)

    @Query("SELECT * FROM drink_type_filter_table WHERE profileId = :profileId")
    suspend fun getDrinkTypeFiltersByProfileId(profileId: Int): List<DrinkTypeFilterEntity>

    @Query("DELETE FROM drink_type_filter_table WHERE profileId = :profileId")
    suspend fun deleteDrinkTypeFiltersByProfileId(profileId: Int)

    @Query("DELETE FROM drink_type_filter_table")
    suspend fun deleteAllDrinkTypeFilters()
}
