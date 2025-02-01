package de.bilkewall.cinder.dto

import kotlinx.serialization.Serializable

@Serializable
data class DrinksDto(
    val drinks: List<DrinkDto>?
)