package de.bilkewall.application.repository

import de.bilkewall.domain.DrinkTypeFilter
import de.bilkewall.domain.IngredientValueFilter

interface SharedFilterRepositoryInterface {
    suspend fun insertDrinkTypeFilter(drinkTypeFilter: DrinkTypeFilter)

    suspend fun getDrinkTypeFiltersByProfileId(profileId: Int): List<DrinkTypeFilter>

    suspend fun deleteDrinkTypeFiltersByProfileId(profileId: Int)

    suspend fun deleteAllDrinkTypeFilters()

    suspend fun insertIngredientValueFilter(ingredientValueFilter: IngredientValueFilter)

    suspend fun getIngredientValueFiltersByProfileId(profileId: Int): List<IngredientValueFilter>

    suspend fun deleteIngredientValueFiltersByProfileId(profileId: Int)

    suspend fun deleteAllIngredientValueFilters()
}