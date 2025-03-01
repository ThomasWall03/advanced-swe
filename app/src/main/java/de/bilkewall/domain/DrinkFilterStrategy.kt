package de.bilkewall.domain

interface DrinkFilterStrategy {
    fun apply(drink: Drink): Boolean
}
