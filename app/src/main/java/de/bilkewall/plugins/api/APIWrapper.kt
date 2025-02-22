package de.bilkewall.plugins.api

import de.bilkewall.domain.Category
import de.bilkewall.domain.Drink
import de.bilkewall.plugins.api.dto.DrinksCategoryDto
import de.bilkewall.plugins.api.dto.DrinksDto
import de.bilkewall.plugins.util.toDrink
import io.ktor.client.call.body
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class APIWrapper(val apiManager: APIManager) {
    suspend fun getDrinkById(id: Int): List<Drink> {
        val drinksDto: DrinksDto = apiManager.get("lookup.php?i=${id}").body()
        return drinksDto.drinks?.map { it.toDrink() } ?: emptyList()
    }

    suspend fun getDrinksByFirstLetter(firstLetter: Char): List<Drink> {
        val drinksDto: DrinksDto = apiManager.get("search.php?f=$firstLetter").body()
        return drinksDto.drinks?.map { it.toDrink() } ?: emptyList()
    }

    suspend fun getDrinksByName(name: String): List<Drink> {
        val drinksDto: DrinksDto = apiManager.get("search.php?s=$name").body()
        return drinksDto.drinks?.map { it.toDrink() } ?: emptyList()
    }

    suspend fun getAllDrinksAtoZ(): List<Drink> = coroutineScope {
        val deferredDrinks = ('a'..'z').map { letter ->
            async { getDrinksByFirstLetter(letter) }
        }
        deferredDrinks.awaitAll().flatten()
    }

    suspend fun getDrinkCategories(): List<Category> {
        val drinksCategoryDto: DrinksCategoryDto = apiManager.get("list.php?c=list").body()
        return drinksCategoryDto.drinks?.map { Category(it.strCategory) } ?: emptyList()
    }
}