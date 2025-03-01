package de.bilkewall.plugins.util

import de.bilkewall.domain.DrinkTypeFilter
import de.bilkewall.domain.IngredientFilter
import de.bilkewall.plugins.database.filter.DrinkTypeFilterEntity
import de.bilkewall.plugins.database.filter.IngredientFilterEntity

fun DrinkTypeFilterEntity.toDrinkTypeFilter(): DrinkTypeFilter =
    DrinkTypeFilter(
        drinkTypeFilterValue = drinkTypeFilterValue,
        profileId = profileId,
    )

fun DrinkTypeFilter.toDrinkTypeFilterEntity(): DrinkTypeFilterEntity =
    DrinkTypeFilterEntity(
        drinkTypeFilterValue = drinkTypeFilterValue,
        profileId = profileId,
    )

fun IngredientFilterEntity.toIngredientValueFilter(): IngredientFilter =
    IngredientFilter(
        ingredientFilterValue = ingredientFilterValue,
        profileId = profileId,
    )

fun IngredientFilter.toIngredientValueFilterEntity(): IngredientFilterEntity =
    IngredientFilterEntity(
        ingredientFilterValue = ingredientFilterValue,
        profileId = profileId,
    )
