package de.bilkewall.plugins.dto

import kotlinx.serialization.Serializable

@Serializable
data class DrinksDto(
    val drinks: List<DrinkDto>?
)