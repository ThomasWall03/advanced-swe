package de.bilkewall.cinder.dto

import kotlinx.serialization.Serializable

@Serializable
data class DrinksCategoryDto (
    val drinks: List<CategoryDto>?
)