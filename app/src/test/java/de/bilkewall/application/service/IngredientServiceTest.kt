package de.bilkewall.application.service

import de.bilkewall.application.repository.drinkingredientcrossref.DrinkIngredientCrossRefFetchingInterface
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class IngredientServiceTest {
    // Mocking
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefFetchingInterface = mock()

    private fun createServiceInstance(): IngredientService {
        val field = IngredientService::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        return IngredientService.getInstance(drinkIngredientCrossRefRepository)
    }

    @Test
    fun `getInstance returns singleton instance`() {
        // Arrange + Act
        val instance1 = IngredientService.getInstance(drinkIngredientCrossRefRepository)
        val instance2 = IngredientService.getInstance(drinkIngredientCrossRefRepository)

        // Assert
        assertSame(instance1, instance2, "getInstance should return the same instance")
    }

    @Test
    fun `getAllIngredientsSortedByName returns sorted ingredients`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val expected = listOf("Rum", "Lime")
        whenever(drinkIngredientCrossRefRepository.getAllIngredientsSortedByName()).thenReturn(
            flowOf(expected)
        )

        // Act
        val actual = target.getAllIngredientsSortedByName().first()

        // Assert
        assertEquals(expected, actual)
    }
}
