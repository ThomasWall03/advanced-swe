package de.bilkewall.application.repository.category

import de.bilkewall.domain.Category

interface CategoryRepositoryManipulatorInterface : CategoryRepositoryFetchingInterface {
    suspend fun insert(category: Category)
}