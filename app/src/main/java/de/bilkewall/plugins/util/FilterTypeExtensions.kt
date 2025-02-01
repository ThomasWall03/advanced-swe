package de.bilkewall.plugins.util

import de.bilkewall.domain.AppDrinkTypeFilter
import de.bilkewall.domain.AppIngredientValueFilter
import de.bilkewall.plugins.database.filter.DrinkTypeFilter
import de.bilkewall.plugins.database.filter.IngredientValueFilter

fun DrinkTypeFilter.toAppDrinkTypeFilter(): AppDrinkTypeFilter {
    return AppDrinkTypeFilter(
        drinkTypeFilterValue = drinkTypeFilterValue,
        profileId = profileId
    )
}

fun AppDrinkTypeFilter.toDrinkTypeFilter(): DrinkTypeFilter {
    return DrinkTypeFilter(
        drinkTypeFilterValue = drinkTypeFilterValue,
        profileId = profileId
    )
}

fun IngredientValueFilter.toAppIngredientValueFilter(): AppIngredientValueFilter {
    return AppIngredientValueFilter(
        ingredientFilterValue = ingredientFilterValue,
        profileId = profileId
    )
}

fun AppIngredientValueFilter.toIngredientValueFilter(): IngredientValueFilter {
    return IngredientValueFilter(
        ingredientFilterValue = ingredientFilterValue,
        profileId = profileId
    )
}