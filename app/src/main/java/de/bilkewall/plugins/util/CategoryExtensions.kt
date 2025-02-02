package de.bilkewall.plugins.util

import de.bilkewall.domain.AppCategory
import de.bilkewall.plugins.database.category.Category

// Converting from Category to AppCategory
fun Category.toAppCategory(): AppCategory {
    return AppCategory(
        strCategory = this.strCategory
    )
}

// Converting from AppCategory to Category
fun AppCategory.toCategory(): Category {
    return Category(
        strCategory = this.strCategory
    )
}