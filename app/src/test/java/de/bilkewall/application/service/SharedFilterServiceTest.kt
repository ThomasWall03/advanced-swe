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
    //Mocking
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
    fun `getIngredientFilterValues returns ingredient filters`() = runTest {
        val profileId = 1
        val ingredientFilters = listOf(
            IngredientFilter("Rum", profileId),
            IngredientFilter("Lime", profileId)
        )
        whenever(sharedFilterRepository.getIngredientFiltersByProfileId(profileId)).thenReturn(ingredientFilters)

        val result = sharedFilterService.getIngredientFilterValues(profileId)

        assertEquals(ingredientFilters, result)
    }

    @Test
    fun `getDrinkTypeFilterValues returns drink type filters`() = runTest {
        val profileId = 1
        val drinkTypeFilters = listOf(
            DrinkTypeFilter("Cocktail", profileId),
            DrinkTypeFilter("Mocktail", profileId)
        )
        whenever(sharedFilterRepository.getDrinkTypeFiltersByProfileId(profileId)).thenReturn(drinkTypeFilters)

        val result = sharedFilterService.getDrinkTypeFilterValues(profileId)

        assertEquals(drinkTypeFilters, result)
    }

    @Test
    fun `saveFiltersForProfile saves filter options`() = runTest {
        val profileId = 1
        val drinkTypeFilters = listOf(
            "Cocktail",
            "Mocktail"
        )
        val ingredientFilters = listOf(
            "Rum",
            "Lime"
        )

        val drinkTypeFilterList = listOf(
            DrinkTypeFilter("Cocktail", profileId),
            DrinkTypeFilter("Mocktail", profileId)
        )

        val ingredientFilterList = listOf(
            IngredientFilter("Rum", profileId),
            IngredientFilter("Lime", profileId)
        )

        sharedFilterService.saveFiltersForProfile(profileId, drinkTypeFilters, ingredientFilters)

        verify(sharedFilterRepository).insertDrinkTypeFilter(drinkTypeFilterList[0])
        verify(sharedFilterRepository).insertDrinkTypeFilter(drinkTypeFilterList[1])
        verify(sharedFilterRepository).insertIngredientFilter(ingredientFilterList[0])
        verify(sharedFilterRepository).insertIngredientFilter(ingredientFilterList[1])
    }

    @Test
    fun `deleteFiltersForProfile deletes filters`() = runTest {
        val profileId = 1

        sharedFilterService.deleteFiltersForProfile(profileId)

        verify(sharedFilterRepository).deleteIngredientValueFiltersByProfileId(profileId)
        verify(sharedFilterRepository).deleteDrinkTypeFiltersByProfileId(profileId)
    }
}