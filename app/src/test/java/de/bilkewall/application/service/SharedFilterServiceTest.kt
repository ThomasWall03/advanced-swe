package de.bilkewall.application.service

import de.bilkewall.application.repository.SharedFilterRepositoryInterface
import de.bilkewall.domain.DrinkTypeFilter
import de.bilkewall.domain.IngredientFilter
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertSame
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class SharedFilterServiceTest {
    // Mocking
    private val sharedFilterRepository: SharedFilterRepositoryInterface = mock()

    private lateinit var sharedFilterService: SharedFilterService

    @Before
    fun setUp() {
        val field = SharedFilterService::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        sharedFilterService = SharedFilterService.getInstance(sharedFilterRepository)
    }

    @Test
    fun `getInstance returns singleton instance`() {
        val instance1 = SharedFilterService.getInstance(sharedFilterRepository)
        val instance2 = SharedFilterService.getInstance(sharedFilterRepository)

        assertSame(instance1, instance2, "getInstance should return the same instance")
    }

    @Test
    fun `getIngredientFilterValues returns ingredient filters`() =
        runTest {
            val profileId = 1
            whenever(sharedFilterRepository.getIngredientFiltersByProfileId(profileId)).thenReturn(testIngredientFilters)

            val result = sharedFilterService.getIngredientFilterValues(profileId)

            assertEquals(testIngredientFilters, result)
        }

    @Test
    fun `getDrinkTypeFilterValues returns drink type filters`() =
        runTest {
            val profileId = 1
            whenever(sharedFilterRepository.getDrinkTypeFiltersByProfileId(profileId)).thenReturn(testDrinkTypeFilters)

            val result = sharedFilterService.getDrinkTypeFilterValues(profileId)

            assertEquals(testDrinkTypeFilters, result)
        }

    @Test
    fun `saveFiltersForProfile saves filter options`() =
        runTest {
            val profileId = 1
            val drinkTypeFilters =
                listOf(
                    "Cocktail",
                    "Mocktail",
                )
            val ingredientFilters =
                listOf(
                    "Rum",
                    "Lime",
                )

            sharedFilterService.saveFiltersForProfile(profileId, drinkTypeFilters, ingredientFilters)

            verify(sharedFilterRepository).insertDrinkTypeFilter(testDrinkTypeFilters[0])
            verify(sharedFilterRepository).insertDrinkTypeFilter(testDrinkTypeFilters[1])
            verify(sharedFilterRepository).insertIngredientFilter(testIngredientFilters[0])
            verify(sharedFilterRepository).insertIngredientFilter(testIngredientFilters[1])
        }

    @Test
    fun `deleteFiltersForProfile deletes filters`() =
        runTest {
            val profileId = 1

            sharedFilterService.deleteFiltersForProfile(profileId)

            verify(sharedFilterRepository).deleteIngredientValueFiltersByProfileId(profileId)
            verify(sharedFilterRepository).deleteDrinkTypeFiltersByProfileId(profileId)
        }

    // TestData
    private val testIngredientFilters =
        listOf(
            IngredientFilter("Rum", 1),
            IngredientFilter("Lime", 1),
        )
    private val testDrinkTypeFilters =
        listOf(
            DrinkTypeFilter("Cocktail", 1),
            DrinkTypeFilter("Mocktail", 1),
        )
}
