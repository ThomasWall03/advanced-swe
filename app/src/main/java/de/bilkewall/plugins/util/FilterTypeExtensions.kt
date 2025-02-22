package de.bilkewall.plugins.util

import de.bilkewall.domain.DrinkTypeFilter
import de.bilkewall.domain.IngredientValueFilter
import de.bilkewall.plugins.database.filter.DrinkTypeFilterEntity
import de.bilkewall.plugins.database.filter.IngredientValueFilterEntity

fun DrinkTypeFilterEntity.toDrinkTypeFilter(): DrinkTypeFilter {
    return DrinkTypeFilter(
        drinkTypeFilterValue = drinkTypeFilterValue,
        profileId = profileId
    )
}

fun DrinkTypeFilter.toDrinkTypeFilterEntity(): DrinkTypeFilterEntity {
    return DrinkTypeFilterEntity(
        drinkTypeFilterValue = drinkTypeFilterValue,
        profileId = profileId
    )
}

fun IngredientValueFilterEntity.toIngredientValueFilter(): IngredientValueFilter {
    return IngredientValueFilter(
        ingredientFilterValue = ingredientFilterValue,
        profileId = profileId
    )
}

fun IngredientValueFilter.toIngredientValueFilterEntity(): IngredientValueFilterEntity {
    return IngredientValueFilterEntity(
        ingredientFilterValue = ingredientFilterValue,
        profileId = profileId
    )
}