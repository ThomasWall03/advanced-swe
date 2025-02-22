package de.bilkewall.plugins.util

import de.bilkewall.domain.Category
import de.bilkewall.plugins.database.category.CategoryEntity

// Converting from Category to AppCategory
fun CategoryEntity.toCategory(): Category {
    return Category(
        strCategory = this.strCategory
    )
}

// Converting from AppCategory to Category
fun Category.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(
        strCategory = this.strCategory
    )
}