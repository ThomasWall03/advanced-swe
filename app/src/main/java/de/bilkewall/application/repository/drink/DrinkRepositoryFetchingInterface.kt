package de.bilkewall.application.repository.drink

import de.bilkewall.domain.Drink
import kotlinx.coroutines.flow.Flow

interface DrinkRepositoryFetchingInterface : DrinkRepositorySingleFetchingInterface {
    suspend fun getDrinkById(drinkId: Int): Drink

    fun getDrinksByName(name: String): Flow<List<Drink>>

    suspend fun getDrinkCount(): Int

    fun getMatchedDrinksByName(
        name: String,
        profileId: Int,
    ): Flow<List<Drink>>

    fun getMatchedDrinksForProfile(profileId: Int): Flow<List<Drink>>
}
