package de.bilkewall.application.repository

import de.bilkewall.domain.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepositoryInterface {
    fun getAllCategories(): Flow<List<Category>>
    suspend fun insert(category: Category)
    suspend fun update(category: Category)
    suspend fun delete(category: Category)
    suspend fun deleteAllCategories()
}