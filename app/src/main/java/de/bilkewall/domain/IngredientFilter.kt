package de.bilkewall.domain

data class IngredientFilter(
    val ingredientFilterValue: String,
    val profileId: Int
)  : DrinkFilterStrategy {
    override fun apply(drink: Drink): Boolean {
        return drink.ingredients.contains(ingredientFilterValue)
    }
}
