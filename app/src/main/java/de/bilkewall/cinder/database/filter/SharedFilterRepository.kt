package de.bilkewall.cinder.database.filter

class SharedFilterRepository (private val drinkTypeFilterDao: DrinkTypeFilterDao, private val ingredientValueFilterDao: IngredientValueFilterDao) {
    suspend fun insertDrinkTypeFilter(drinkTypeFilter: DrinkTypeFilter) {
        drinkTypeFilterDao.insertDrinkTypeFilter(drinkTypeFilter)
    }

    suspend fun getDrinkTypeFiltersByProfileId(profileId: Int): List<DrinkTypeFilter> {
        return drinkTypeFilterDao.getDrinkTypeFiltersByProfileId(profileId)
    }

    suspend fun deleteDrinkTypeFiltersByProfileId(profileId: Int) {
        drinkTypeFilterDao.deleteDrinkTypeFiltersByProfileId(profileId)
    }

    suspend fun deleteAllDrinkTypeFilters() {
        drinkTypeFilterDao.deleteAllDrinkTypeFilters()
    }

    suspend fun insertIngredientValueFilter(ingredientValueFilter: IngredientValueFilter) {
        ingredientValueFilterDao.insertIngredientValueFilter(ingredientValueFilter)
    }

    suspend fun getIngredientValueFiltersByProfileId(profileId: Int): List<IngredientValueFilter> {
        return ingredientValueFilterDao.getIngredientValueFiltersByProfileId(profileId)
    }

    suspend fun deleteIngredientValueFiltersByProfileId(profileId: Int) {
        ingredientValueFilterDao.deleteIngredientValueFiltersByProfileId(profileId)
    }

    suspend fun deleteAllIngredientValueFilters() {
        ingredientValueFilterDao.deleteAllIngredientValueFilters()
    }
}