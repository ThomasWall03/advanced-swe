package de.bilkewall.application.service

import de.bilkewall.domain.Drink
import de.bilkewall.domain.DrinkFilterStrategy
import de.bilkewall.domain.DrinkTypeFilter
import de.bilkewall.domain.IngredientFilter
import de.bilkewall.domain.Match
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class DrinkFilterServiceTest {
    // Mocking
    private val drinkFetchingService: DrinkFetchingService = mock()

    private fun createServiceInstance(): DrinkFilterService {
        val field = DrinkFilterService::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        return DrinkFilterService.getInstance(drinkFetchingService)
    }

    @Test
    fun `getInstance returns singleton instance`() {
        //Arrange + Act
        val instance1 = DrinkFilterService.getInstance(drinkFetchingService)
        val instance2 = DrinkFilterService.getInstance(drinkFetchingService)

        //Assert
        assertSame(instance1, instance2, "getInstance should return the same instance")
    }

    @Test
    fun `evaluateCurrentDrink returns evaluated drink`() = runTest {
        // Arrange
        val givenBypassFilter = false
        val target = createServiceInstance()
        val expected = mockDrinks
        val givenFilters = mockFilters
        val givenMatches = mockMatchesNotCorrespondingToDrinks
        whenever(drinkFetchingService.getAllDrinks()).thenReturn(flowOf(expected))

        // Act
        val actual = target.evaluateCurrentDrinks(givenBypassFilter, givenMatches, givenFilters)

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `evaluateCurrentDrink returns no drink due to all drinks being matched`() = runTest {
        // Arrange
        val givenBypassFilter = false
        val target = createServiceInstance()
        val expected = emptyList<Drink>()
        val givenMatches = mockMatchesCorrespondingToDrinks
        val givenFilters = mockFilters
        whenever(drinkFetchingService.getAllDrinks()).thenReturn(flowOf(mockDrinks))

        // Act
        val actual = target.evaluateCurrentDrinks(givenBypassFilter, givenMatches, givenFilters)

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `evaluateCurrentDrink returns evaluated drink for bypass filter`() = runTest {
        // Arrange
        val givenBypassFilter = true
        val target = createServiceInstance()
        val expected = mockDrinks
        val givenMatches = mockMatchesNotCorrespondingToDrinks
        val givenFilters = emptyList<List<DrinkFilterStrategy>>()
        whenever(drinkFetchingService.getAllDrinks()).thenReturn(flowOf(mockDrinks))

        // Act
        val actual = target.evaluateCurrentDrinks(givenBypassFilter, givenMatches, givenFilters)

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `isAllDrinkMatched returns true if all drinks are matched`() = runTest {
        // Arrange
        val givenBypassFilter = true
        val target = createServiceInstance()
        val givenDrinks = emptyList<Drink>()
        val expected = true

        // Act
        val actual = target.areAllDrinksMatched(givenBypassFilter, givenDrinks)

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `isAllDrinkMatched returns false if not all drinks are matched`() = runTest {
        // Arrange
        val givenBypassFilter = true
        val target = createServiceInstance()
        val givenDrinks = mockDrinks
        val expected = false

        // Act
        val actual = target.areAllDrinksMatched(givenBypassFilter, givenDrinks)

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `noMoreDrinksAvailable returns true if the list is empty`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val givenDrinks = emptyList<Drink>()
        val expected = true

        // Act
        val actual = target.noMoreDrinksAvailable(givenDrinks)

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
    private val mockMatchesNotCorrespondingToDrinks =
        listOf(
            Match(drinkId = 3, profileId = 1, outcome = true),
            Match(drinkId = 4, profileId = 1, outcome = true),
        )
    private val mockMatchesCorrespondingToDrinks =
        listOf(
            Match(drinkId = 1, profileId = 1, outcome = true),
            Match(drinkId = 2, profileId = 1, outcome = true),
        )
    private val mockFilters = listOf(listOf(IngredientFilter("Rum", 1)), listOf(DrinkTypeFilter("Cocktail", 1)))
}
