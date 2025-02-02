package de.bilkewall.application.repository

import de.bilkewall.domain.AppCategory
import kotlinx.coroutines.flow.Flow

interface CategoryRepositoryInterface {
    fun getAllCategories(): Flow<List<AppCategory>>
    suspend fun insert(category: AppCategory)
    suspend fun update(category: AppCategory)
    suspend fun delete(category: AppCategory)
    suspend fun deleteAllCategories()
}