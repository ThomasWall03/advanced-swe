package de.bilkewall.domain

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class IngredientFilterTest {
    @Test
    fun `apply returns true when drink contains ingredient`() {
        // Arrange
        val given = Drink(ingredients = listOf("Rum", "Lime"))
        val target = IngredientFilter(ingredientFilterValue = "Rum", profileId = 1)

        // Act
        val actual = target.apply(given)

        // Assert
        assertTrue(actual, "apply should return true when drink contains the ingredient")
    }

    @Test
    fun `apply returns false when drink does not contain ingredient`() {
        // Arrange
        val given = Drink(ingredients = listOf("Vodka", "Lime"))
        val target = IngredientFilter(ingredientFilterValue = "Rum", profileId = 1)

        // Act
        val actual = target.apply(given)

        // Assert
        assertFalse(actual, "apply should return false when drink does not contain the ingredient")
    }

}
