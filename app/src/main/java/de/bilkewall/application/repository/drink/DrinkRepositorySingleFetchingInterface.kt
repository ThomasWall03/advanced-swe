package de.bilkewall.application.repository.drink

import de.bilkewall.domain.Drink
import kotlinx.coroutines.flow.Flow

interface DrinkRepositorySingleFetchingInterface {
    fun getAllDrinks(): Flow<List<Drink>>
}