package de.bilkewall.application.service

import de.bilkewall.application.repository.CategoryRepositoryInterface

class CategoryService private constructor(
    private val categoryRepository: CategoryRepositoryInterface,
) {
    companion object {
        @Volatile
        private var instance: CategoryService? = null

        fun getInstance(categoryRepository: CategoryRepositoryInterface): CategoryService =
            instance ?: synchronized(this) {
                instance ?: CategoryService(categoryRepository).also {
                    instance = it
                }
            }
    }

    fun getAllCategories() = categoryRepository.getAllCategories()
}
