package de.bilkewall.domain

data class DrinkTypeFilter(
    val drinkTypeFilterValue: String,
    val profileId: Int
) : DrinkFilterStrategy {
    override fun apply(drink: Drink): Boolean {
        return drinkTypeFilterValue == drink.categoryName
    }
}
