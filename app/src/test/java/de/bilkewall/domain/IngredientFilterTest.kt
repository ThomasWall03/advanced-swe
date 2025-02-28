package de.bilkewall.domain

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class IngredientFilterTest {
    @Test
    fun `apply returns true when drink contains ingredient`() {
        val drink = Drink(ingredients = listOf("Rum", "Lime"))
        val filter = IngredientFilter(ingredientFilterValue = "Rum", profileId = 1)

        assertTrue(filter.apply(drink))
    }

    @Test
    fun `apply returns false when drink does not contain ingredient`() {
        val drink = Drink(ingredients = listOf("Vodka", "Lime"))
        val filter = IngredientFilter(ingredientFilterValue = "Rum", profileId = 1)

        assertFalse(filter.apply(drink))
    }
}
