package de.bilkewall.plugins.util

import de.bilkewall.domain.DrinkTypeFilter
import de.bilkewall.domain.IngredientFilter
import de.bilkewall.plugins.database.filter.DrinkTypeFilterEntity
import de.bilkewall.plugins.database.filter.IngredientFilterEntity

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

fun IngredientFilterEntity.toIngredientValueFilter(): IngredientFilter {
    return IngredientFilter(
        ingredientFilterValue = ingredientFilterValue,
        profileId = profileId
    )
}

fun IngredientFilter.toIngredientValueFilterEntity(): IngredientFilterEntity {
    return IngredientFilterEntity(
        ingredientFilterValue = ingredientFilterValue,
        profileId = profileId
    )
}