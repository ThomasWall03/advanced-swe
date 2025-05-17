package de.bilkewall.application.repository.drink

import de.bilkewall.domain.Drink

interface DrinkRepositoryManipulatorInterface : DrinkRepositorySingleFetchingInterface {
    suspend fun insert(drink: Drink)

    suspend fun update(drink: Drink)

    suspend fun deleteAllDrinks()
}