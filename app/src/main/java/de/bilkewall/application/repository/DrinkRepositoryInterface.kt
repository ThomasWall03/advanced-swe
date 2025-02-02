package de.bilkewall.application.repository

import de.bilkewall.domain.AppDrink
import kotlinx.coroutines.flow.Flow

interface DrinkRepositoryInterface {
    fun getAllDrinks(): Flow<List<AppDrink>>

    suspend fun insert(drink: AppDrink)

    suspend fun update(drink: AppDrink)

    suspend fun getDrinkById(drinkId: Int): AppDrink

    suspend fun getDrinksByName(name: String): Flow<List<AppDrink>>

    suspend fun delete(drink: AppDrink)

    suspend fun deleteAllDrinks()

    suspend fun getDrinkCount(): Int

    fun getMatchedDrinksByName(name: String): Flow<List<AppDrink>>

    fun getMatchedDrinksForProfile(profileId: Int): Flow<List<AppDrink>>
}