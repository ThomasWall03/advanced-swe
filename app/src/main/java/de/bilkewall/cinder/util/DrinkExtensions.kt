package de.bilkewall.cinder.util

import de.bilkewall.cinder.database.drink.Drink
import de.bilkewall.cinder.database.drinkIngredientCrossRef.DrinkIngredientCrossRef
import de.bilkewall.cinder.dto.AppDrinkDto
import de.bilkewall.cinder.dto.DrinkDto

fun DrinkDto.toDrinkAndRelations(): Pair<Drink, List<DrinkIngredientCrossRef>> {
    val drink = Drink(
        drinkId = idDrink.toInt(),
        drinkName = strDrink,
        videoUrl = strVideo ?: "",
        categoryName = strCategory,
        alcoholic = strAlcoholic,
        glassType = strGlass,
        instructionsEN = strInstructions,
        instructionsDE = strInstructionsDE ?: "",
        thumbnailUrl = strDrinkThumb,
        dateModified = dateModified ?: ""
    )

    val ingredientsList = mutableListOf<DrinkIngredientCrossRef>()
    val ingredientFields = listOf(
        strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
        strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
        strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15
    )
    val measureFields = listOf(
        strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
        strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
        strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15
    )

    for (i in ingredientFields.indices) {
        val ingredientName = ingredientFields[i]
        val measurement = measureFields[i]

        if (!ingredientName.isNullOrEmpty()) {
            ingredientsList.add(
                DrinkIngredientCrossRef(
                    drinkId = idDrink.toInt(),
                    ingredientName = ingredientName,
                    unit = measurement?: ""
                )
            )
        }
    }

    return Pair(drink, ingredientsList)
}

private fun parseMeasurement(measurementString: String): Pair<Int, String> {
    val ambiguousTerms = listOf("garnish with", "pinch", "coarse", "juice of", "dash")

    val lowerMeasure = measurementString.lowercase()
    if (ambiguousTerms.any { it in lowerMeasure }) {
        return Pair(-1, measurementString.trim())
    }

    val regex = Regex("""([\d/.\s]+)\s*(\D+)""")
    val matchResult = regex.find(measurementString.trim())

    return if (matchResult != null) {
        val (amountStr, unit) = matchResult.destructured

        val amount = parseMeasurementAmount(amountStr)
        Pair(amount, unit.trim())
    } else {
        Pair(-1, measurementString.trim())
    }
}

private fun parseMeasurementAmount(amountStr: String): Int {
    return if (amountStr.contains("/")) {
        amountStr.split(" ").map { part ->
            if ("/" in part) {
                val (numerator, denominator) = part.split("/")
                    .map { it.toDoubleOrNull() ?: 0.0 }
                if (denominator != 0.0) numerator / denominator else 0.0
            } else {
                part.toDoubleOrNull() ?: 0.0
            }
        }.sum().toInt()
    } else {
        amountStr.trim().toDoubleOrNull()?.toInt() ?: -1
    }
}

fun Drink.toAppDrinkDto(ingredients: List<String>, measurements: List<String>): AppDrinkDto {
    return AppDrinkDto(
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
        measurements = measurements
    )
}

fun AppDrinkDto.toDrink(): Drink {
    return Drink(
        drinkId = drinkId,
        drinkName = drinkName,
        videoUrl = videoUrl,
        categoryName = categoryName,
        alcoholic = alcoholic,
        glassType = glassType,
        instructionsEN = instructionsEN,
        instructionsDE = instructionsDE,
        thumbnailUrl = thumbnailUrl,
        dateModified = dateModified
    )
}

fun DrinkDto.toAppDrinkDto(): AppDrinkDto {
    val ingredientsList = listOfNotNull(
        strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
        strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
        strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15
    )

    val measurementsList = listOfNotNull(
        strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
        strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
        strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15
    )

    return AppDrinkDto(
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
        measurements = measurementsList
    )
}


