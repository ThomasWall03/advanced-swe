package de.bilkewall.plugins.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class DrinksDto(
    val drinks: List<DrinkDto>?,
)
