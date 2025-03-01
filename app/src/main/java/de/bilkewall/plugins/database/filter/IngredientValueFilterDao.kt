package de.bilkewall.plugins.database.filter

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IngredientValueFilterDao {
    @Insert
    suspend fun insertIngredientValueFilter(ingredientValueFilter: IngredientFilterEntity)

    @Query("SELECT * FROM ingredient_value_filter_table WHERE profileId = :profileId")
    suspend fun getIngredientValueFiltersByProfileId(profileId: Int): List<IngredientFilterEntity>

    @Query("DELETE FROM ingredient_value_filter_table WHERE profileId = :profileId")
    suspend fun deleteIngredientValueFiltersByProfileId(profileId: Int)

    @Query("DELETE FROM ingredient_value_filter_table")
    suspend fun deleteAllIngredientValueFilters()
}
