package de.bilkewall.cinder.dto

data class AppDrinkDto(
    val drinkId: Int = 0,
    val drinkName: String = "",
    val videoUrl: String = "",
    val categoryName: String = "",
    val alcoholic: String = "",
    val glassType: String = "",
    val instructionsEN: String = "",
    val instructionsDE: String = "",
    val thumbnailUrl: String = "",
    val dateModified: String = "",
    val ingredients: List<String> = emptyList(),
    val measurements: List<String> = emptyList()
)
