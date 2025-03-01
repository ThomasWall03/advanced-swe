package de.bilkewall.application.service

import de.bilkewall.application.repository.CategoryRepositoryInterface
import de.bilkewall.domain.Category
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class CategoryServiceTest {
    // Mocking
    private val categoryRepository: CategoryRepositoryInterface = mock()

    private lateinit var categoryService: CategoryService

    @Before
    fun setup() {
        val field = CategoryService::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        categoryService = CategoryService.getInstance(categoryRepository)
    }

    @Test
    fun `getInstance returns singleton instance`() {
        val instance1 = CategoryService.getInstance(categoryRepository)
        val instance2 = CategoryService.getInstance(categoryRepository)

        assertSame(instance1, instance2, "getInstance should return the same instance")
    }

    @Test
    fun `getAllCategories returns categories`() =
        runTest {
            val categories =
                listOf(
                    Category("Cocktail"),
                    Category("Mocktail"),
                )
            whenever(categoryRepository.getAllCategories()).thenReturn(flowOf(categories))

            val result = categoryService.getAllCategories().first()

            assertEquals(categories, result)
        }
}
