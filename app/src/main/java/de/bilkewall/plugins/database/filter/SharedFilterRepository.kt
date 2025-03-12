package de.bilkewall.plugins.database.filter

import de.bilkewall.application.repository.SharedFilterRepositoryInterface
import de.bilkewall.domain.DrinkTypeFilter
import de.bilkewall.domain.IngredientFilter
import de.bilkewall.plugins.util.toDrinkTypeFilter
import de.bilkewall.plugins.util.toDrinkTypeFilterEntity
import de.bilkewall.plugins.util.toIngredientValueFilter
import de.bilkewall.plugins.util.toIngredientValueFilterEntity

class SharedFilterRepository(
    private val drinkTypeFilterDao: DrinkTypeFilterDao,
    private val ingredientValueFilterDao: IngredientValueFilterDao,
) : SharedFilterRepositoryInterface {
    override suspend fun insertDrinkTypeFilter(drinkTypeFilter: DrinkTypeFilter) {
        drinkTypeFilterDao.insertDrinkTypeFilter(drinkTypeFilter.toDrinkTypeFilterEntity())
    }

    override suspend fun getDrinkTypeFiltersByProfileId(profileId: Int): List<DrinkTypeFilter> =
        drinkTypeFilterDao
            .getDrinkTypeFiltersByProfileId(profileId)
            .map { it.toDrinkTypeFilter() }

    override suspend fun deleteDrinkTypeFiltersByProfileId(profileId: Int) {
        drinkTypeFilterDao.deleteDrinkTypeFiltersByProfileId(profileId)
    }

    override suspend fun insertIngredientFilter(ingredientFilter: IngredientFilter) {
        ingredientValueFilterDao.insertIngredientValueFilter(ingredientFilter.toIngredientValueFilterEntity())
    }

    override suspend fun getIngredientFiltersByProfileId(profileId: Int): List<IngredientFilter> =
        ingredientValueFilterDao
            .getIngredientValueFiltersByProfileId(profileId)
            .map { it.toIngredientValueFilter() }

    override suspend fun deleteIngredientValueFiltersByProfileId(profileId: Int) {
        ingredientValueFilterDao.deleteIngredientValueFiltersByProfileId(profileId)
    }
}
