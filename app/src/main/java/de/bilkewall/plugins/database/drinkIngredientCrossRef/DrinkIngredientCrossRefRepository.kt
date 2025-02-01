package de.bilkewall.plugins.database.drinkIngredientCrossRef

import kotlinx.coroutines.flow.Flow

class DrinkIngredientCrossRefRepository (private val drinkIngredientDao: DrinkIngredientDao){
    val allIngredients: Flow<List<String>> = drinkIngredientDao.getAllIngredientsSortedByName()

    suspend fun insert(drinkIngredientCrossRef: DrinkIngredientCrossRef) {
        drinkIngredientDao.insertDrinkIngredientCrossRef(drinkIngredientCrossRef)
    }

    suspend fun deleteAllRelationsOfADrink(drinkId: Int) {
        drinkIngredientDao.deleteAllRelationsOfADrink(drinkId)
    }

    suspend fun deleteAllRelations(){
        drinkIngredientDao.deleteAllDrinkIngredientCrossRefs()
    }

    suspend fun getIngredientsForDrink(id: Int): List<DrinkIngredientCrossRef> {
        return drinkIngredientDao.getIngredientsForDrink(id)
    }
}