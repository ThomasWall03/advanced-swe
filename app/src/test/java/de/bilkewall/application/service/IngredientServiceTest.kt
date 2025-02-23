package de.bilkewall.application.service

import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class IngredientServiceTest {
    //Mocking
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface = mock()

    private lateinit var ingredientService: IngredientService
    @Before
    fun setUp() {
        val field = IngredientService::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        ingredientService = IngredientService.getInstance(drinkIngredientCrossRefRepository)
    }

    @Test
    fun `getInstance returns singleton instance`() {
        val instance1 = IngredientService.getInstance(drinkIngredientCrossRefRepository)
        val instance2 = IngredientService.getInstance(drinkIngredientCrossRefRepository)

        assertSame(instance1, instance2, "getInstance should return the same instance")
    }

    @Test
    fun `getAllIngredientsSortedByName returns sorted ingredients`() = runTest {
        val ingredients = listOf(
            "Rum",
            "Lime"
        )
        whenever(drinkIngredientCrossRefRepository.getAllIngredientsSortedByName()).thenReturn(flowOf(ingredients))

        val result = ingredientService.getAllIngredientsSortedByName().first()

        assertEquals(ingredients, result)
    }
}