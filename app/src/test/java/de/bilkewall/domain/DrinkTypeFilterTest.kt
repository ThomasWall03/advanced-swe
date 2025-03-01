package de.bilkewall.domain

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class DrinkTypeFilterTest {
    @Test
    fun `apply returns true when drink category matches filter`() {
        val drink =
            Drink(
                drinkId = 1,
                drinkName = "Mojito",
                categoryName = "Cocktail",
                ingredients = listOf("Rum", "Lime"),
                measurements = listOf("1", "1"),
            )
        val filter = DrinkTypeFilter(drinkTypeFilterValue = "Cocktail", profileId = 1)

        assertTrue(filter.apply(drink), "apply should return true when drink category matches the filter")
    }

    @Test
    fun `apply returns false when drink category does not match filter`() {
        val drink =
            Drink(
                drinkId = 2,
                drinkName = "Martini",
                categoryName = "Martini",
                ingredients = listOf("Vodka", "Vermouth"),
                measurements = listOf("1", "1"),
            )
        val filter = DrinkTypeFilter(drinkTypeFilterValue = "Cocktail", profileId = 1)

        assertFalse(filter.apply(drink), "apply should return false when drink category does not match the filter")
    }
}
