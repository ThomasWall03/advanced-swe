package de.bilkewall.plugins.database.drink

import de.bilkewall.application.repository.drink.DrinkRepositoryFetchingInterface
import de.bilkewall.application.repository.drink.DrinkRepositoryManipulatorInterface
import de.bilkewall.domain.Drink
import de.bilkewall.plugins.util.toDrink
import de.bilkewall.plugins.util.toDrinkEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DrinkRepository(
    private val drinkDao: DrinkDao,
) : DrinkRepositoryFetchingInterface, DrinkRepositoryManipulatorInterface {
    override fun getAllDrinks() = drinkDao.getAllDrinks().map { drinks -> drinks.map { it.toDrink() } }

    override suspend fun insert(drink: Drink) {
        drinkDao.insert(drink.toDrinkEntity())
    }

    override suspend fun update(drink: Drink) {
        drinkDao.update(drink.toDrinkEntity())
    }

    override suspend fun deleteAllDrinks() {
        drinkDao.deleteAllDrinks()
    }

    override suspend fun getDrinkById(drinkId: Int): Drink = drinkDao.getDrinkById(drinkId).toDrink()

    override fun getDrinksByName(name: String): Flow<List<Drink>> =
        drinkDao.getDrinksByName(name).map { drinks -> drinks.map { it.toDrink() } }

    override suspend fun getDrinkCount(): Int = drinkDao.getDrinkCount()

    override fun getMatchedDrinksByName(
        name: String,
        profileId: Int,
    ): Flow<List<Drink>> =
        drinkDao.getMatchedDrinksByName(name, profileId).map { drinks ->
            drinks.map {
                it.toDrink()
            }
        }

    override fun getMatchedDrinksForProfile(profileId: Int): Flow<List<Drink>> =
        drinkDao.getMatchedDrinksForProfile(profileId).map { drinks ->
            drinks.map {
                it.toDrink()
            }
        }
}
