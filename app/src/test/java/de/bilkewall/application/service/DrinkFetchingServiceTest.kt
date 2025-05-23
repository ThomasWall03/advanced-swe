package de.bilkewall.application.service

import de.bilkewall.application.repository.drink.DrinkRepositoryFetchingInterface
import de.bilkewall.application.repository.drinkingredientcrossref.DrinkIngredientCrossRefFetchingInterface
import de.bilkewall.domain.Drink
import de.bilkewall.domain.DrinkIngredientCrossRef
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class DrinkFetchingServiceTest {
    // Mocking
    private val drinkRepository: DrinkRepositoryFetchingInterface = mock()
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefFetchingInterface = mock()

    private fun createServiceInstance(): DrinkFetchingService {
        val field = DrinkFetchingService::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        return DrinkFetchingService.getInstance(drinkRepository, drinkIngredientCrossRefRepository)
    }

    @Test
    fun `getInstance returns singleton instance`() {
        //Arrange + Act
        val instance1 = DrinkFetchingService.getInstance(drinkRepository, drinkIngredientCrossRefRepository)
        val instance2 = DrinkFetchingService.getInstance(drinkRepository, drinkIngredientCrossRefRepository)

        //Assert
        assertSame(instance1, instance2, "getInstance should return the same instance")
    }

    @Test
    fun `getDrinkById returns drink with ingredients`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val givenId = 1
        val givenIngredients = mockIngredients
        val expected = mockDrinks[0]
        whenever(drinkRepository.getDrinkById(givenId)).thenReturn(expected)
        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(givenId)).thenReturn(givenIngredients)

        // Act
        val actual = target.getDrinkById(givenId)

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `getAllDrinks returns all drinks`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val givenIngredients = mockIngredients
        val expected = mockDrinks
        whenever(drinkRepository.getAllDrinks()).thenReturn(flowOf(expected))
        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(1)).thenReturn(givenIngredients)
        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(2)).thenReturn(givenIngredients)

        // Act
        val actual = target.getAllDrinks().first()

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `getDrinksByName returns drinks with given name`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val givenName = "Mojito"
        val givenIngredients = mockIngredients
        val expected = mockDrinks
        whenever(drinkRepository.getDrinksByName(givenName)).thenReturn(flowOf(expected))
        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(1)).thenReturn(givenIngredients)
        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(2)).thenReturn(givenIngredients)

        // Act
        val actual = target.getDrinksByName(givenName).first()

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `getDrinkCount returns count of drinks`() = runTest {
        // Arrange
        val expected = 2
        val target = createServiceInstance()
        whenever(drinkRepository.getDrinkCount()).thenReturn(expected)

        // Act
        val actual = target.getDrinkCount()

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `getMatchedDrinksByName returns matched drinks for given name and profile`() = runTest {
        // Arrange
        val givenName = "Mojito"
        val givenProfileId = 1
        val givenIngredients = mockIngredients
        val expected = mockDrinks
        val target = createServiceInstance()
        whenever(drinkRepository.getMatchedDrinksByName(givenName, givenProfileId)).thenReturn(flowOf(expected))
        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(1)).thenReturn(givenIngredients)
        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(2)).thenReturn(givenIngredients)

        // Act
        val actual = target.getMatchedDrinksByNameAndProfile(givenName, givenProfileId).first()

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `getMatchedDrinksByName throws error upon invalid input name`() = runTest {
        // Arrange
        val givenName = ""
        val givenProfileId = 1
        val target = createServiceInstance()

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            target.getMatchedDrinksByNameAndProfile(givenName, givenProfileId).first()
        }

        // Verify the exception message
        assertEquals("Name cannot be empty", exception.message)
    }

    @Test
    fun `getMatchedDrinksByName throws error upon invalid profile id`() = runTest {
        // Arrange
        val givenName = "validName"
        val givenProfileId = -1
        val target = createServiceInstance()

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            target.getMatchedDrinksByNameAndProfile(givenName, givenProfileId).first()
        }

        // Verify the exception message
        assertEquals("Profile ID cannot be negative", exception.message)
    }

    @Test
    fun `getMatchedDrinksForProfile returns matched drinks for given profile`() = runTest {
        // Arrange
        val givenProfileId = 1
        val givenIngredients = mockIngredients
        val expected = mockDrinks
        val target = createServiceInstance()
        whenever(drinkRepository.getMatchedDrinksForProfile(givenProfileId)).thenReturn(flowOf(expected))
        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(1)).thenReturn(givenIngredients)
        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(2)).thenReturn(givenIngredients)

        // Act
        val actual = target.getMatchedDrinksForProfile(givenProfileId).first()

        // Assert
        assertEquals(expected, actual)
    }

    // Test Data
    private val mockDrinks =
        listOf(
            Drink(
                drinkId = 1,
                drinkName = "Mojito",
                categoryName = "Cocktail",
                ingredients = listOf("Rum", "Lime"),
                measurements = listOf("1", "1"),
            ),
            Drink(
                drinkId = 2,
                drinkName = "Martini",
                categoryName = "Cocktail",
                ingredients = listOf("Rum", "Lime"),
                measurements = listOf("1", "1"),
            ),
        )
    private val mockIngredients =
        listOf(
            DrinkIngredientCrossRef(1, "Rum", "1"),
            DrinkIngredientCrossRef(1, "Lime", "1"),
        )
}
