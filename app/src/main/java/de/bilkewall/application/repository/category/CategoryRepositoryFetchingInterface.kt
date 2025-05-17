package de.bilkewall.application.repository.category

import de.bilkewall.domain.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepositoryFetchingInterface {
    fun getAllCategories(): Flow<List<Category>>
}
