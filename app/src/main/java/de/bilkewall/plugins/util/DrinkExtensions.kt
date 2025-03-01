package de.bilkewall.plugins.util

import de.bilkewall.domain.Drink
import de.bilkewall.plugins.api.dto.DrinkDto
import de.bilkewall.plugins.database.drink.DrinkEntity

fun DrinkDto.toDrink(): Drink {
    val ingredientsList =
        listOfNotNull(
            strIngredient1,
            strIngredient2,
            strIngredient3,
            strIngredient4,
            strIngredient5,
            strIngredient6,
            strIngredient7,
            strIngredient8,
            strIngredient9,
            strIngredient10,
            strIngredient11,
            strIngredient12,
            strIngredient13,
            strIngredient14,
            strIngredient15,
        )

    val measurementsList =
        listOfNotNull(
            strMeasure1,
            strMeasure2,
            strMeasure3,
            strMeasure4,
            strMeasure5,
            strMeasure6,
            strMeasure7,
            strMeasure8,
            strMeasure9,
            strMeasure10,
            strMeasure11,
            strMeasure12,
            strMeasure13,
            strMeasure14,
            strMeasure15,
        )

    return Drink(
        drinkId = idDrink.toInt(),
        drinkName = strDrink,
        videoUrl = strVideo ?: "",
        categoryName = strCategory,
        alcoholic = strAlcoholic,
        glassType = strGlass,
        instructionsEN = strInstructions,
        instructionsDE = strInstructionsDE ?: "",
        thumbnailUrl = strDrinkThumb,
        dateModified = dateModified ?: "",
        ingredients = ingredientsList,
        measurements = measurementsList,
    )
}

fun Drink.toDrinkEntity(): DrinkEntity =
    DrinkEntity(
        drinkId = drinkId,
        drinkName = drinkName,
        videoUrl = videoUrl,
        categoryName = categoryName,
        alcoholic = alcoholic,
        glassType = glassType,
        instructionsEN = instructionsEN,
        instructionsDE = instructionsDE,
        thumbnailUrl = thumbnailUrl,
        dateModified = dateModified,
    )

fun DrinkEntity.toDrink(
    ingredients: List<String> = emptyList(),
    measurements: List<String> = emptyList(),
): Drink =
    Drink(
        drinkId = drinkId,
        drinkName = drinkName,
        videoUrl = videoUrl,
        categoryName = categoryName,
        alcoholic = alcoholic,
        glassType = glassType,
        instructionsEN = instructionsEN,
        instructionsDE = instructionsDE,
        thumbnailUrl = thumbnailUrl,
        dateModified = dateModified,
        ingredients = ingredients,
        measurements = measurements,
    )
