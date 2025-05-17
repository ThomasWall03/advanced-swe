package de.bilkewall.plugins.database.category

import de.bilkewall.application.repository.category.CategoryRepositoryFetchingInterface
import de.bilkewall.application.repository.category.CategoryRepositoryManipulatorInterface
import de.bilkewall.domain.Category
import de.bilkewall.plugins.util.toCategory
import de.bilkewall.plugins.util.toCategoryEntity
import kotlinx.coroutines.flow.map

class CategoryRepository(
    private val categoryDao: CategoryDao,
) : CategoryRepositoryFetchingInterface, CategoryRepositoryManipulatorInterface {
    override fun getAllCategories() = categoryDao.getAllCategories().map { categories -> categories.map { it.toCategory() } }

    override suspend fun insert(category: Category) {
        categoryDao.insert(category.toCategoryEntity())
    }
}
