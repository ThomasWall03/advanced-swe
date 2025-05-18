package de.bilkewall.domain

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class DrinkTypeFilterTest {
    @Test
    fun `apply returns true when drink category matches filter`() {
        // Arrange
        val given = Drink(
            drinkId = 1,
            drinkName = "Mojito",
            categoryName = "Cocktail",
            ingredients = listOf("Rum", "Lime"),
            measurements = listOf("1", "1"),
        )
        val target = DrinkTypeFilter(drinkTypeFilterValue = "Cocktail", profileId = 1)

        // Act
        val actual = target.apply(given)

        // Assert
        assertTrue(actual, "apply should return true when drink category matches the filter")
    }

    @Test
    fun `apply returns false when drink category does not match filter`() {
        // Arrange
        val given = Drink(
            drinkId = 2,
            drinkName = "Martini",
            categoryName = "Martini",
            ingredients = listOf("Vodka", "Vermouth"),
            measurements = listOf("1", "1"),
        )
        val target = DrinkTypeFilter(drinkTypeFilterValue = "Cocktail", profileId = 1)

        // Act
        val actual = target.apply(given)

        // Assert
        assertFalse(actual, "apply should return false when drink category does not match the filter")
    }

}
