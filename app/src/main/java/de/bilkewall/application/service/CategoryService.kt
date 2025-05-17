package de.bilkewall.application.service

import de.bilkewall.application.repository.category.CategoryRepositoryFetchingInterface

class CategoryService private constructor(
    private val categoryRepository: CategoryRepositoryFetchingInterface,
) {
    companion object {
        @Volatile
        private var instance: CategoryService? = null

        fun getInstance(categoryRepository: CategoryRepositoryFetchingInterface): CategoryService =
            instance ?: synchronized(this) {
                instance ?: CategoryService(categoryRepository).also {
                    instance = it
                }
            }
    }

    fun getAllCategories() = categoryRepository.getAllCategories()
}
