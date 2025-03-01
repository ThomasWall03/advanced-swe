package de.bilkewall.application.service

import de.bilkewall.domain.Drink
import de.bilkewall.domain.DrinkTypeFilter
import de.bilkewall.domain.IngredientFilter
import de.bilkewall.domain.Match
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class DrinkFilterServiceTest {
    // Mocking
    private val drinkFetchingService: DrinkFetchingService = mock()

    private lateinit var drinkFilterService: DrinkFilterService

    @Before
    fun setUp() {
        val field = DrinkFilterService::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        drinkFilterService = DrinkFilterService.getInstance(drinkFetchingService)
    }

    @Test
    fun `getInstance returns singleton instance`() {
        val instance1 = DrinkFilterService.getInstance(drinkFetchingService)
        val instance2 = DrinkFilterService.getInstance(drinkFetchingService)

        assertSame(instance1, instance2, "getInstance should return the same instance")
    }

    @Test
    fun `evaluateCurrentDrink returns evaluated drink`() =
        runTest {
            val bypassFilter = false
            whenever(drinkFetchingService.getAllDrinks()).thenReturn(flowOf(testDrinks))

            val result = drinkFilterService.evaluateCurrentDrinks(bypassFilter, testMatches, filters)

            assertEquals(testDrinks, result)
        }

    @Test
    fun `evaluateCurrentDrink returns evaluated drink for bypass filter`() =
        runTest {
            val bypassFilter = true
            whenever(drinkFetchingService.getAllDrinks()).thenReturn(flowOf(testDrinks))

            val result = drinkFilterService.evaluateCurrentDrinks(bypassFilter, testMatches, emptyList())

            assertEquals(testDrinks, result)
        }

    @Test
    fun `isAllDrinkMatched returns true if all drinks are matched`() =
        runTest {
            val bypassFilter = true
            val result = drinkFilterService.areAllDrinksMatched(bypassFilter, emptyList())

            assertTrue(result)
        }

    @Test
    fun `noMoreDrinksAvailable returns true if the list is empty`() =
        runTest {
            val result = drinkFilterService.noMoreDrinksAvailable(emptyList())

            assertTrue(result)
        }

    // Test Data
    private val testDrinks =
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
    private val testMatches =
        listOf(
            Match(drinkId = 3, profileId = 1, outcome = true),
            Match(drinkId = 4, profileId = 1, outcome = true),
        )
    private val filters = listOf(listOf(IngredientFilter("Rum", 1)), listOf(DrinkTypeFilter("Cocktail", 1)))
}
