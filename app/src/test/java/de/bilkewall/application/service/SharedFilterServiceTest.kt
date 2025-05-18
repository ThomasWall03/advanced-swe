package de.bilkewall.application.service

import de.bilkewall.application.repository.SharedFilterRepositoryInterface
import de.bilkewall.domain.DrinkTypeFilter
import de.bilkewall.domain.IngredientFilter
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertSame
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class SharedFilterServiceTest {
    // Mocking
    private val sharedFilterRepository: SharedFilterRepositoryInterface = mock()

    private fun createServiceInstance(): SharedFilterService {
        val field = SharedFilterService::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        return SharedFilterService.getInstance(sharedFilterRepository)
    }

    @Test
    fun `getInstance returns singleton instance`() {
        // Arrange + Act
        val instance1 = SharedFilterService.getInstance(sharedFilterRepository)
        val instance2 = SharedFilterService.getInstance(sharedFilterRepository)

        // Assert
        assertSame(instance1, instance2, "getInstance should return the same instance")
    }

    @Test
    fun `getIngredientFilterValues returns ingredient filters`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val profileId = 1
        val expected = mockIngredientFilters
        whenever(sharedFilterRepository.getIngredientFiltersByProfileId(profileId)).thenReturn(expected)

        // Act
        val actual = target.getIngredientFilterValues(profileId)

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `getDrinkTypeFilterValues returns drink type filters`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val profileId = 1
        val expected = mockDrinkTypeFilters
        whenever(sharedFilterRepository.getDrinkTypeFiltersByProfileId(profileId)).thenReturn(expected)

        // Act
        val actual = target.getDrinkTypeFilterValues(profileId)

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `saveFiltersForProfile saves filter options`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val profileId = 1
        val givenDrinkTypeFilters = listOf("Cocktail", "Mocktail")
        val givenIngredientFilters = listOf("Rum", "Lime")

        // Act
        target.saveFiltersForProfile(profileId, givenDrinkTypeFilters, givenIngredientFilters)

        // Assert
        verify(sharedFilterRepository).insertDrinkTypeFilter(mockDrinkTypeFilters[0])
        verify(sharedFilterRepository).insertDrinkTypeFilter(mockDrinkTypeFilters[1])
        verify(sharedFilterRepository).insertIngredientFilter(mockIngredientFilters[0])
        verify(sharedFilterRepository).insertIngredientFilter(mockIngredientFilters[1])
    }

    @Test
    fun `deleteFiltersForProfile deletes filters`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val profileId = 1

        // Act
        target.deleteFiltersForProfile(profileId)

        // Assert
        verify(sharedFilterRepository).deleteIngredientValueFiltersByProfileId(profileId)
        verify(sharedFilterRepository).deleteDrinkTypeFiltersByProfileId(profileId)
    }

    // TestData
    private val mockIngredientFilters =
        listOf(
            IngredientFilter("Rum", 1),
            IngredientFilter("Lime", 1),
        )
    private val mockDrinkTypeFilters =
        listOf(
            DrinkTypeFilter("Cocktail", 1),
            DrinkTypeFilter("Mocktail", 1),
        )
}
