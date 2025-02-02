package de.bilkewall.application.repository

import de.bilkewall.domain.AppDrinkTypeFilter
import de.bilkewall.domain.AppIngredientValueFilter

interface SharedFilterRepositoryInterface {
    suspend fun insertDrinkTypeFilter(drinkTypeFilter: AppDrinkTypeFilter)

    suspend fun getDrinkTypeFiltersByProfileId(profileId: Int): List<AppDrinkTypeFilter>

    suspend fun deleteDrinkTypeFiltersByProfileId(profileId: Int)

    suspend fun deleteAllDrinkTypeFilters()

    suspend fun insertIngredientValueFilter(ingredientValueFilter: AppIngredientValueFilter)

    suspend fun getIngredientValueFiltersByProfileId(profileId: Int): List<AppIngredientValueFilter>

    suspend fun deleteIngredientValueFiltersByProfileId(profileId: Int)

    suspend fun deleteAllIngredientValueFilters()
}