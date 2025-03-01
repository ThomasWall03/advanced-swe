package de.bilkewall.plugins.util

import de.bilkewall.domain.DrinkIngredientCrossRef
import de.bilkewall.plugins.database.drinkIngredientCrossRef.DrinkIngredientCrossRefEntity

fun DrinkIngredientCrossRefEntity.toDrinkIngredientCrossRef(): DrinkIngredientCrossRef =
    DrinkIngredientCrossRef(
        drinkId = drinkId,
        ingredientName = ingredientName,
        unit = unit,
    )

fun DrinkIngredientCrossRef.toDrinkIngredientCrossRefEntity(): DrinkIngredientCrossRefEntity =
    DrinkIngredientCrossRefEntity(
        drinkId = drinkId,
        ingredientName = ingredientName,
        unit = unit,
    )
