package de.bilkewall.application.service

import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.domain.Drink
import de.bilkewall.domain.DrinkIngredientCrossRef
import de.bilkewall.domain.DrinkTypeFilter
import de.bilkewall.domain.IngredientFilter
import de.bilkewall.domain.Match
import de.bilkewall.main.di.DependencyProvider.drinkIngredientCrossRefRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class DrinkServiceTest {
//    //Mocking
//    private val drinkRepository: DrinkRepositoryInterface = mock()
//    private val drinkFilterService: DrinkFilterService = mock()
//    private val drinkFetchingService: DrinkFetchingService = mock()
//
//    private lateinit var drinkService: DrinkService
//
//    @Before
//    fun setUp() {
//        val field = DrinkService::class.java.getDeclaredField("instance")
//        field.isAccessible = true
//        field.set(null, null)
//
//        drinkService = DrinkService.getInstance(drinkRepository, drinkFilterService, drinkFetchingService)
//    }
//
//    @Test
//    fun `getInstance returns singleton instance`() {
//        val instance1 = DrinkService.getInstance(drinkRepository, drinkFilterService, drinkFetchingService)
//        val instance2 = DrinkService.getInstance(drinkRepository,drinkFilterService, drinkFetchingService)
//
//        assertSame(instance1, instance2, "getInstance should return the same instance")
//    }
//
//    @Test
//    fun `getDrinkById returns drink with ingredients`() = runTest {
////        expectMockCallsForGetDrinkById(1, testDrinks[0], testIngredients)
////
////        val result = drinkService.getDrinkById(1)
////
////        assertEquals(testDrinks[0], result)
//    }
//
//    @Test
//    fun `getAllDrinks returns all drinks`() = runTest {
//        whenever(drinkRepository.getAllDrinks()).thenReturn(flowOf(testDrinks))
//
//        expectMockCallsForGetDrinkById(1, testDrinks[0], testIngredients)
//        expectMockCallsForGetDrinkById(2, testDrinks[1], testIngredients)
//
//        val result = drinkService.getAllDrinks().first()
//
//        assertEquals(testDrinks, result)
//    }
//
//    @Test
//    fun `getDrinksByName returns drinks with given name`() = runTest {
//        whenever(drinkRepository.getDrinksByName("Mojito")).thenReturn(flowOf(testDrinks))
//        expectMockCallsForGetDrinkById(1, testDrinks[0], testIngredients)
//        expectMockCallsForGetDrinkById(2, testDrinks[1], testIngredients)
//
//        val result = drinkService.getDrinksByName("Mojito").first()
//
//        assertEquals(testDrinks, result)
//    }
//
//    @Test
//    fun `getDrinkCount returns count of drinks`() = runTest {
//        val drinkCount = 2
//        whenever(drinkRepository.getDrinkCount()).thenReturn(drinkCount)
//
//        val result = drinkService.getDrinkCount()
//
//        assertEquals(drinkCount, result)
//    }
//
//    @Test
//    fun `getMatchedDrinksByName returns matched drinks for given name and profile`() = runTest {
//        val profileId = 1
//        whenever(drinkRepository.getMatchedDrinksByName("Mojito", profileId)).thenReturn(flowOf(testDrinks))
//        expectMockCallsForGetDrinkById(1, testDrinks[0], testIngredients)
//        expectMockCallsForGetDrinkById(2, testDrinks[1], testIngredients)
//
//        val result = drinkService.getMatchedDrinksByNameAndProfile("Mojito", profileId).first()
//
//        assertEquals(testDrinks, result)
//    }
//
//    @Test
//    fun `getMatchedDrinksForProfile returns matched drinks for given profile`() = runTest {
//        val profileId = 1
//        whenever(drinkRepository.getMatchedDrinksForProfile(profileId)).thenReturn(flowOf(testDrinks))
//        expectMockCallsForGetDrinkById(1, testDrinks[0], testIngredients )
//        expectMockCallsForGetDrinkById(2, testDrinks[1], testIngredients )
//
//        val result = drinkService.getMatchedDrinksForProfile(profileId).first()
//
//        assertEquals(testDrinks, result)
//    }
//
//    @Test
//    fun `evaluateCurrentDrink returns evaluated drink`() = runTest {
////        val bypassFilter = false
////
////        whenever(drinkRepository.getAllDrinks()).thenReturn(flowOf(testDrinks))
////        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(1)).thenReturn(testIngredients)
////        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(2)).thenReturn(testIngredients)
////
////        expectMockCallsForGetDrinkById(1, testDrinks[0], testIngredients)
////        expectMockCallsForGetDrinkById(2, testDrinks[1], testIngredients)
////
////        val result = drinkService.evaluateCurrentDrink(bypassFilter, testMatchesDontCorrespondsWithDrinks, testIngredientFilters, testDrinkTypeFilters)
////
////        assertEquals(testDrinks[0], result)
//    }
//
//    @Test
//    fun `evaluateCurrentDrink returns evaluated drink for bypass filter`() = runTest {
////        val bypassFilter = true
////
////        whenever(drinkRepository.getAllDrinks()).thenReturn(flowOf(testDrinks))
////        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(1)).thenReturn(testIngredients)
////        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(2)).thenReturn(testIngredients)
////
////        expectMockCallsForGetDrinkById(1, testDrinks[0], testIngredients)
////        expectMockCallsForGetDrinkById(2, testDrinks[1], testIngredients)
////
////        val result = drinkService.evaluateCurrentDrink(bypassFilter, testMatchesDontCorrespondsWithDrinks, emptyList(), emptyList())
////
////        assertEquals(testDrinks[0], result)
//    }
//
//    @Test
//    fun `isAllDrinkMatched returns true if all drinks are matched`() = runTest {
////        val bypassFilter = true
////
////        whenever(drinkRepository.getAllDrinks()).thenReturn(flowOf(testDrinks))
////        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(1)).thenReturn(testIngredients)
////        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(2)).thenReturn(testIngredients)
////
////        expectMockCallsForGetDrinkById(0, Drink(), emptyList())
////
////        drinkService.evaluateCurrentDrink(bypassFilter, testMatchesCorrespondsWithDrinks, emptyList(), emptyList())
////
////        val result = drinkService.areAllDrinksMatched(bypassFilter)
////
////        assertTrue(result)
//    }
//
//    //Helper Functions
//    private suspend fun expectMockCallsForGetDrinkById(drinkId: Int, drink: Drink, ingredients: List<DrinkIngredientCrossRef>) {
//        whenever(drinkRepository.getDrinkById(drinkId)).thenReturn(drink)
//        whenever(drinkIngredientCrossRefRepository.getIngredientsForDrink(drinkId)).thenReturn(ingredients)
//    }
//
//    //Test Data
//    private val testDrinks = listOf(
//        Drink(drinkId = 1, drinkName = "Mojito", categoryName = "Cocktail", ingredients = listOf("Rum", "Lime"), measurements = listOf("1", "1")),
//        Drink(drinkId = 2, drinkName = "Martini", categoryName = "Cocktail", ingredients = listOf("Rum", "Lime"), measurements = listOf("1", "1"))
//    )
//    private val testIngredients =  listOf(
//        DrinkIngredientCrossRef(1, "Rum", "1"),
//        DrinkIngredientCrossRef(1, "Lime", "1")
//    )
//    private val testMatchesCorrespondsWithDrinks = listOf(
//        Match(drinkId = 1, profileId = 1, outcome = true),
//        Match(drinkId = 2, profileId = 1, outcome = true)
//    )
//    private val testMatchesDontCorrespondsWithDrinks = listOf(
//        Match(drinkId = 3, profileId = 1, outcome = true),
//        Match(drinkId = 4, profileId = 1, outcome = true)
//    )
//    private val testIngredientFilters = listOf(IngredientFilter("Rum", 1))
//    private val testDrinkTypeFilters = listOf(DrinkTypeFilter("Cocktail", 1))

}