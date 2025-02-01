package de.bilkewall.plugins.dto

import kotlinx.serialization.Serializable

@Serializable
data class DrinksCategoryDto (
    val drinks: List<CategoryDto>?
)