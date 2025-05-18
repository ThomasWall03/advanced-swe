package de.bilkewall.plugins.database.drink

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkDao {
    @Query("SELECT * FROM drink_table")
    fun getAllDrinks(): Flow<List<DrinkEntity>>

    @Insert
    suspend fun insert(drink: DrinkEntity)

    @Update
    suspend fun update(drink: DrinkEntity)

    @Query("SELECT * FROM drink_table WHERE drinkId = :drinkId")
    suspend fun getDrinkById(drinkId: Int): DrinkEntity

    @Query("DELETE FROM drink_table")
    suspend fun deleteAllDrinks()

    @Query("SELECT COUNT(*) FROM drink_table")
    suspend fun getDrinkCount(): Int

    @Query("SELECT * FROM drink_table WHERE drinkName LIKE '%' || :name || '%'")
    fun getDrinksByName(name: String): Flow<List<DrinkEntity>>

    @Query(
        """
        SELECT d.*
        FROM drink_table d
        INNER JOIN match_table m ON d.drinkId = m.drinkId
        WHERE d.drinkName LIKE '%' || :name || '%' AND m.profileId = :profileId AND m.outcome = 1
    """,
    )
    fun getMatchedDrinksByName(
        name: String,
        profileId: Int,
    ): Flow<List<DrinkEntity>>

    @Query(
        """
        SELECT d.*
        FROM drink_table d
        INNER JOIN match_table m ON d.drinkId = m.drinkId
        WHERE m.profileId = :profileId and m.outcome = 1
    """,
    )
    fun getMatchedDrinksForProfile(profileId: Int): Flow<List<DrinkEntity>>
}
