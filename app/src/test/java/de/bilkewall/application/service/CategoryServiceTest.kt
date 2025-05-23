package de.bilkewall.application.service

import de.bilkewall.application.repository.category.CategoryRepositoryFetchingInterface
import de.bilkewall.domain.Category
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class CategoryServiceTest {
    // Mocking
    private val categoryRepository: CategoryRepositoryFetchingInterface = mock()

    private fun createServiceInstance(): CategoryService{
        val field = CategoryService::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        return CategoryService.getInstance(categoryRepository)
    }

    @Test
    fun `getInstance returns singleton instance`() {
        //Arrange + Act
        val instance1 = CategoryService.getInstance(categoryRepository)
        val instance2 = CategoryService.getInstance(categoryRepository)

        //Assert
        assertSame(instance1, instance2, "getInstance should return the same instance")
    }

    @Test
    fun `getAllCategories returns categories`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val givenCategories = listOf(
            Category("Cocktail"),
            Category("Mocktail"),
        )
        val expected = givenCategories
        whenever(categoryRepository.getAllCategories()).thenReturn(flowOf(givenCategories))

        // Act
        val actual = target.getAllCategories().first()

        // Assert
        assertEquals(expected, actual)
    }
}