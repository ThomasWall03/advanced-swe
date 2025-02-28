package de.bilkewall.application.repository

import de.bilkewall.domain.Drink
import kotlinx.coroutines.flow.Flow

interface DrinkRepositoryInterface {
    fun getAllDrinks(): Flow<List<Drink>>

    suspend fun insert(drink: Drink)

    suspend fun update(drink: Drink)

    suspend fun getDrinkById(drinkId: Int): Drink

    fun getDrinksByName(name: String): Flow<List<Drink>>

    suspend fun delete(drink: Drink)

    suspend fun deleteAllDrinks()

    suspend fun getDrinkCount(): Int

    fun getMatchedDrinksByName(name: String, profileId: Int): Flow<List<Drink>>

    fun getMatchedDrinksForProfile(profileId: Int): Flow<List<Drink>>
}