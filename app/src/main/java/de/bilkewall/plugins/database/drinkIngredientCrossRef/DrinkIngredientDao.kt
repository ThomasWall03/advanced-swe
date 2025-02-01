package de.bilkewall.plugins.database.drinkIngredientCrossRef

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkIngredientDao {
    @Insert
    suspend fun insertDrinkIngredientCrossRef(crossRef: DrinkIngredientCrossRef)

    @Update
    suspend fun updateDrinkIngredientCrossRef(crossRef: DrinkIngredientCrossRef)

    @Delete
    suspend fun deleteDrinkIngredientCrossRef(crossRef: DrinkIngredientCrossRef)

    @Query("DELETE FROM drink_ingredient_cross_ref")
    suspend fun deleteAllDrinkIngredientCrossRefs()

    @Query("DELETE FROM drink_ingredient_cross_ref WHERE drinkId = :drinkId")
    suspend fun deleteAllRelationsOfADrink(drinkId: Int)

    @Query("SELECT DISTINCT ingredientName FROM drink_ingredient_cross_ref ORDER BY ingredientName")
    fun getAllIngredientsSortedByName(): Flow<List<String>>

    @Query("SELECT DISTINCT ingredientName FROM drink_ingredient_cross_ref")
    fun getAllIngredients(): Flow<List<String>>

    @Query("SELECT * FROM drink_ingredient_cross_ref WHERE drinkId = :drinkId")
    suspend fun getIngredientsForDrink(drinkId: Int): List<DrinkIngredientCrossRef>
}
