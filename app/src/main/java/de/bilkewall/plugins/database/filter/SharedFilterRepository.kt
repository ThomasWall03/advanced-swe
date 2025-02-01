package de.bilkewall.plugins.database.filter

import de.bilkewall.adapters.repository.SharedFilterRepositoryInterface
import de.bilkewall.domain.AppDrinkTypeFilter
import de.bilkewall.domain.AppIngredientValueFilter
import de.bilkewall.plugins.util.toAppDrinkTypeFilter
import de.bilkewall.plugins.util.toAppIngredientValueFilter
import de.bilkewall.plugins.util.toDrinkTypeFilter
import de.bilkewall.plugins.util.toIngredientValueFilter

class SharedFilterRepository(
    private val drinkTypeFilterDao: DrinkTypeFilterDao,
    private val ingredientValueFilterDao: IngredientValueFilterDao
) : SharedFilterRepositoryInterface {
    override suspend fun insertDrinkTypeFilter(drinkTypeFilter: AppDrinkTypeFilter) {
        drinkTypeFilterDao.insertDrinkTypeFilter(drinkTypeFilter.toDrinkTypeFilter())
    }

    override suspend fun getDrinkTypeFiltersByProfileId(profileId: Int): List<AppDrinkTypeFilter> {
        return drinkTypeFilterDao.getDrinkTypeFiltersByProfileId(profileId)
            .map { it.toAppDrinkTypeFilter() }
    }

    override suspend fun deleteDrinkTypeFiltersByProfileId(profileId: Int) {
        drinkTypeFilterDao.deleteDrinkTypeFiltersByProfileId(profileId)
    }

    override suspend fun deleteAllDrinkTypeFilters() {
        drinkTypeFilterDao.deleteAllDrinkTypeFilters()
    }

    override suspend fun insertIngredientValueFilter(ingredientValueFilter: AppIngredientValueFilter) {
        ingredientValueFilterDao.insertIngredientValueFilter(ingredientValueFilter.toIngredientValueFilter())
    }

    override suspend fun getIngredientValueFiltersByProfileId(profileId: Int): List<AppIngredientValueFilter> {
        return ingredientValueFilterDao.getIngredientValueFiltersByProfileId(profileId)
            .map { it.toAppIngredientValueFilter() }
    }

    override suspend fun deleteIngredientValueFiltersByProfileId(profileId: Int) {
        ingredientValueFilterDao.deleteIngredientValueFiltersByProfileId(profileId)
    }

    override suspend fun deleteAllIngredientValueFilters() {
        ingredientValueFilterDao.deleteAllIngredientValueFilters()
    }
}