package de.bilkewall.plugins.database.drink

import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.domain.AppDrink
import de.bilkewall.plugins.util.toAppDrink
import de.bilkewall.plugins.util.toDrink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DrinkRepository(private val drinkDao: DrinkDao) : DrinkRepositoryInterface {
    override fun getAllDrinks() =
        drinkDao.getAllDrinks().map { drinks -> drinks.map { it.toAppDrink() } }

    override suspend fun insert(drink: AppDrink) {
        drinkDao.insert(drink.toDrink())
    }

    override suspend fun update(drink: AppDrink) {
        drinkDao.update(drink.toDrink())
    }

    override suspend fun delete(drink: AppDrink) {
        drinkDao.delete(drink.toDrink())
    }

    override suspend fun deleteAllDrinks() {
        drinkDao.deleteAllDrinks()
    }

    override suspend fun getDrinkById(id: Int): AppDrink {
        return drinkDao.getDrinkById(id).toAppDrink()
    }

    override suspend fun getDrinksByName(name: String): Flow<List<AppDrink>> {
        return drinkDao.getDrinksByName(name).map { drinks -> drinks.map { it.toAppDrink() } }
    }

    override suspend fun getDrinkCount(): Int {
        return drinkDao.getDrinkCount()
    }

    override fun getMatchedDrinksByName(name: String): Flow<List<AppDrink>> {
        return drinkDao.getMatchedDrinksByName(name).map { drinks -> drinks.map { it.toAppDrink() } }
    }
}