package de.bilkewall.plugins.database.category

import de.bilkewall.application.repository.CategoryRepositoryInterface
import de.bilkewall.domain.AppCategory
import de.bilkewall.plugins.util.toAppCategory
import de.bilkewall.plugins.util.toCategory
import kotlinx.coroutines.flow.map

class CategoryRepository (private val categoryDao: CategoryDao) : CategoryRepositoryInterface{
    override fun getAllCategories() =
        categoryDao.getAllCategories().map { categories -> categories.map { it.toAppCategory() } }

    override suspend fun insert(category: AppCategory) {
        categoryDao.insert(category.toCategory())
    }

    override suspend fun update(category: AppCategory) {
        categoryDao.update(category.toCategory())
    }

    override suspend fun delete(category: AppCategory) {
        categoryDao.delete(category.toCategory())
    }

    override suspend fun deleteAllCategories() {
        categoryDao.deleteAllCategories()
    }
}