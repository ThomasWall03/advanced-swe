package de.bilkewall.plugins.database.filter

import de.bilkewall.application.repository.SharedFilterRepositoryInterface
import de.bilkewall.domain.DrinkTypeFilter
import de.bilkewall.domain.IngredientValueFilter
import de.bilkewall.plugins.util.toDrinkTypeFilter
import de.bilkewall.plugins.util.toDrinkTypeFilterEntity
import de.bilkewall.plugins.util.toIngredientValueFilter
import de.bilkewall.plugins.util.toIngredientValueFilterEntity

class SharedFilterRepository(
    private val drinkTypeFilterDao: DrinkTypeFilterDao,
    private val ingredientValueFilterDao: IngredientValueFilterDao
) : SharedFilterRepositoryInterface {
    override suspend fun insertDrinkTypeFilter(drinkTypeFilter: DrinkTypeFilter) {
        drinkTypeFilterDao.insertDrinkTypeFilter(drinkTypeFilter.toDrinkTypeFilterEntity())
    }

    override suspend fun getDrinkTypeFiltersByProfileId(profileId: Int): List<DrinkTypeFilter> {
        return drinkTypeFilterDao.getDrinkTypeFiltersByProfileId(profileId)
            .map { it.toDrinkTypeFilter() }
    }

    override suspend fun deleteDrinkTypeFiltersByProfileId(profileId: Int) {
        drinkTypeFilterDao.deleteDrinkTypeFiltersByProfileId(profileId)
    }

    override suspend fun deleteAllDrinkTypeFilters() {
        drinkTypeFilterDao.deleteAllDrinkTypeFilters()
    }

    override suspend fun insertIngredientValueFilter(ingredientValueFilter: IngredientValueFilter) {
        ingredientValueFilterDao.insertIngredientValueFilter(ingredientValueFilter.toIngredientValueFilterEntity())
    }

    override suspend fun getIngredientValueFiltersByProfileId(profileId: Int): List<IngredientValueFilter> {
        return ingredientValueFilterDao.getIngredientValueFiltersByProfileId(profileId)
            .map { it.toIngredientValueFilter() }
    }

    override suspend fun deleteIngredientValueFiltersByProfileId(profileId: Int) {
        ingredientValueFilterDao.deleteIngredientValueFiltersByProfileId(profileId)
    }

    override suspend fun deleteAllIngredientValueFilters() {
        ingredientValueFilterDao.deleteAllIngredientValueFilters()
    }
}