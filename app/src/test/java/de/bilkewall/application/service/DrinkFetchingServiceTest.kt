package de.bilkewall.application.service

import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.domain.Drink
import de.bilkewall.domain.DrinkIngredientCrossRef
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class DrinkFetchingServiceTest {
    // Mocking
    private val drinkRepository: DrinkRepositoryInterface = mock()
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface = mock()

    private lateinit var drinkFetchingService: DrinkFetchingService

    @Before
    fun setUp() {
        val field = DrinkFetchingService::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        drinkFetchingService = DrinkFetchingService.getInstance(drinkRepository, drinkIngredientCrossRefRepository)
    }

    @Test
    fun `getInstance returns singleton instance`() {
        val instance1 = DrinkFetchingService.getInstance(drinkRepository, drinkIngredientCrossRefRepository)
        val instance2 = DrinkFetchingService.getInstance(drinkRepository, drinkIngredientCrossRefRepository)

        assertSame(instance1, instance2, "getInstance should return the same instance")
    }

    @Test
    fun `getDrinkById returns drink with ingredients`() =
        runTest {
            whenever(drinkRepository.getDrinkById(1)).thenReturn(testDrinks[0])
            expectMockCallsForAddIngredientsToDrink(1, testIngredients)

            val result = drinkFetchingService.getDrinkById(1)

            assertEquals(testDrinks[0], result)
        }

    @Test
    fun `getAllDrinks returns all drinks`() =
        runTest {
            whenever(drinkRepository.getAllDrinks()).thenReturn(flowOf(testDrinks))

            expectMockCallsForAddIngredientsToDrink(1, testIngredients)
            expectMockCallsForAddIngredientsToDrink(2, testIngredients)

            val result = drinkFetchingService.getAllDrinks().first()

            assertEquals(testDrinks, result)
        }

    @Test
    fun `getDrinksByName returns drinks with given name`() =
        runTest {
            whenever(drinkRepository.getDrinksByName("Mojito")).thenReturn(flowOf(testDrinks))
            expectMockCallsForAddIngredientsToDrink(1, testIngredients)
            expectMockCallsForAddIngredientsToDrink(2, testIngredients)

            val result = drinkFetchingService.getDrinksByName("Mojito").first()

            assertEquals(testDrinks, result)
        }

    @Test
    fun `getDrinkCount returns count of drinks`() =
        runTest {
            val drinkCount = 2
            whenever(drinkRepository.getDrinkCount()).thenReturn(drinkCount)

            val result = drinkFetchingService.getDrinkCount()

            assertEquals(drinkCount, result)
        }

    @Test
    fun `getMatchedDrinksByName returns matched drinks for given name and profile`() =
        runTest {
            val profileId = 1
            whenever(drinkRepository.getMatchedDrinksByName("Mojito", profileId)).thenReturn(flowOf(testDrinks))
            expectMockCallsForAddIngredientsToDrink(1, testIngredients)
            expectMockCallsForAddIngredientsToDrink(2, testIngredients)

            val result = drinkFetchingService.getMatchedDrinksByNameAndProfile("Mojito", profileId).first()

            assertEquals(testDrinks, result)
        }

    @Test
    fun `getMatchedDrinksForProfile returns matched drinks for given profile`() =
        runTest {
            val profileId = 1
            whenever(drinkRepository.getMatchedDrinksForProfile(profileId)).thenReturn(flowOf(testDrinks))
            expectMockCallsForAddIngredientsToDrink(1, testIngredients)
            expectMockCallsForAddIngredientsToDrink(2, testIngredients)

            val result = drinkFetchingService.getMatchedDrinksForProfile(profileId).first()

            assertEquals(testDrinks, result)
        }

    // Helper Functions
    private suspend fun expectMockCallsForAddIngredientsToDrink(
        drinkId: Int,
        ingredients: List<DrinkIngredientCrossRef>,
    ) {
        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(drinkId)).thenReturn(ingredients)
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
    private val testIngredients =
        listOf(
            DrinkIngredientCrossRef(1, "Rum", "1"),
            DrinkIngredientCrossRef(1, "Lime", "1"),
        )
}
