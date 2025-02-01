package de.bilkewall.plugins.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class DrinksCategoryDto(
    val drinks: List<CategoryDto>?
)