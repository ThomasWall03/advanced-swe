package de.bilkewall.cinder.database.drink

class DrinkRepository (private val drinkDao: DrinkDao) {
    val allDrinks = drinkDao.getAllDrinks()

    suspend fun insert(drink: Drink) {
        drinkDao.insert(drink)
    }

    suspend fun update(drink: Drink) {
        drinkDao.update(drink)
    }

    suspend fun delete(drink: Drink) {
        drinkDao.delete(drink)
    }

    suspend fun deleteAllDrinks() {
        drinkDao.deleteAllDrinks()
    }

    suspend fun getDrinkById(id: Int): Drink{
        return drinkDao.getDrinkById(id)
    }

    suspend fun getDrinkCount(): Int {
        return drinkDao.getDrinkCount()
    }
}