package de.bilkewall.cinder.database.drink

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkDao {
    @Query("SELECT * FROM drink_table")
    fun getAllDrinks(): Flow<List<Drink>>

    @Insert
    suspend fun insert(drink: Drink)

    @Update
    suspend fun update(drink: Drink)

    @Query("SELECT * FROM drink_table WHERE drinkId = :drinkId")
    suspend fun getDrinkById(drinkId: Int): Drink

    @Delete
    suspend fun delete(drink: Drink)

    @Query("DELETE FROM drink_table")
    suspend fun deleteAllDrinks()

    @Query("SELECT COUNT(*) FROM drink_table")
    suspend fun getDrinkCount(): Int
}