package de.bilkewall.application.service

import de.bilkewall.application.repository.CategoryRepositoryInterface

class CategoryService (
    private val categoryRepository: CategoryRepositoryInterface
){
    fun getAllCategories() = categoryRepository.getAllCategories()
}