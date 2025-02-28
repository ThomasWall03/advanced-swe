package de.bilkewall.application.repository

import de.bilkewall.domain.DrinkTypeFilter
import de.bilkewall.domain.IngredientFilter

interface SharedFilterRepositoryInterface {
    suspend fun insertDrinkTypeFilter(drinkTypeFilter: DrinkTypeFilter)

    suspend fun getDrinkTypeFiltersByProfileId(profileId: Int): List<DrinkTypeFilter>

    suspend fun deleteDrinkTypeFiltersByProfileId(profileId: Int)

    suspend fun deleteAllDrinkTypeFilters()

    suspend fun insertIngredientFilter(ingredientFilter: IngredientFilter)

    suspend fun getIngredientFiltersByProfileId(profileId: Int): List<IngredientFilter>

    suspend fun deleteIngredientValueFiltersByProfileId(profileId: Int)

    suspend fun deleteAllIngredientValueFilters()
}