package de.bilkewall.plugins.database.drinkIngredientCrossRef

import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.domain.DrinkIngredientCrossRef
import de.bilkewall.plugins.util.toDrinkIngredientCrossRef
import de.bilkewall.plugins.util.toDrinkIngredientCrossRefEntity
import kotlinx.coroutines.flow.Flow

class DrinkIngredientCrossRefRepository(
    private val drinkIngredientDao: DrinkIngredientDao,
) : DrinkIngredientCrossRefInterface {
    private val allIngredients: Flow<List<String>> = drinkIngredientDao.getAllIngredientsSortedByName()

    override fun getAllIngredientsSortedByName(): Flow<List<String>> = allIngredients

    override suspend fun insert(drinkIngredientCrossRef: DrinkIngredientCrossRef) {
        drinkIngredientDao.insertDrinkIngredientCrossRef(drinkIngredientCrossRef.toDrinkIngredientCrossRefEntity())
    }

    override suspend fun deleteAllRelationsOfADrink(drinkId: Int) {
        drinkIngredientDao.deleteAllRelationsOfADrink(drinkId)
    }

    override suspend fun deleteAllRelations() {
        drinkIngredientDao.deleteAllDrinkIngredientCrossRefs()
    }

    override suspend fun getIngredientsForDrink(id: Int): List<DrinkIngredientCrossRef> =
        drinkIngredientDao
            .getIngredientsForDrink(id)
            .map { it.toDrinkIngredientCrossRef() }
}
