package de.bilkewall.plugins.util

import de.bilkewall.domain.AppDrinkIngredientCrossRef
import de.bilkewall.plugins.database.drinkIngredientCrossRef.DrinkIngredientCrossRef

fun DrinkIngredientCrossRef.toAppDrinkIngredientCrossRef(): AppDrinkIngredientCrossRef {
    return AppDrinkIngredientCrossRef(
        drinkId = drinkId,
        ingredientName = ingredientName,
        unit = unit
    )
}

fun AppDrinkIngredientCrossRef.toDrinkIngredientCrossRef(): DrinkIngredientCrossRef {
    return DrinkIngredientCrossRef(
        drinkId = drinkId,
        ingredientName = ingredientName,
        unit = unit
    )
}