package de.bilkewall.plugins.database.category

import de.bilkewall.application.repository.CategoryRepositoryInterface
import de.bilkewall.domain.Category
import de.bilkewall.plugins.util.toCategory
import de.bilkewall.plugins.util.toCategoryEntity
import kotlinx.coroutines.flow.map

class CategoryRepository (private val categoryDao: CategoryDao) : CategoryRepositoryInterface{
    override fun getAllCategories() =
        categoryDao.getAllCategories().map { categories -> categories.map { it.toCategory() } }

    override suspend fun insert(category: Category) {
        categoryDao.insert(category.toCategoryEntity())
    }

    override suspend fun update(category: Category) {
        categoryDao.update(category.toCategoryEntity())
    }

    override suspend fun delete(category: Category) {
        categoryDao.delete(category.toCategoryEntity())
    }

    override suspend fun deleteAllCategories() {
        categoryDao.deleteAllCategories()
    }
}