package de.bilkewall.plugins.database.drinkIngredientCrossRef

import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.domain.AppDrinkIngredientCrossRef
import de.bilkewall.plugins.util.toAppDrinkIngredientCrossRef
import de.bilkewall.plugins.util.toDrinkIngredientCrossRef
import kotlinx.coroutines.flow.Flow

class DrinkIngredientCrossRefRepository(private val drinkIngredientDao: DrinkIngredientDao) :
    DrinkIngredientCrossRefInterface {
    val allIngredients: Flow<List<String>> = drinkIngredientDao.getAllIngredientsSortedByName()

    override fun getAllIngredientsSortedByName(): Flow<List<String>> {
        return allIngredients
    }

    override suspend fun insert(drinkIngredientCrossRef: AppDrinkIngredientCrossRef) {
        drinkIngredientDao.insertDrinkIngredientCrossRef(drinkIngredientCrossRef.toDrinkIngredientCrossRef())
    }

    override suspend fun deleteAllRelationsOfADrink(drinkId: Int) {
        drinkIngredientDao.deleteAllRelationsOfADrink(drinkId)
    }

    override suspend fun deleteAllRelations() {
        drinkIngredientDao.deleteAllDrinkIngredientCrossRefs()
    }

    override suspend fun getIngredientsForDrink(id: Int): List<AppDrinkIngredientCrossRef> {
        return drinkIngredientDao.getIngredientsForDrink(id)
            .map { it.toAppDrinkIngredientCrossRef() }
    }
}