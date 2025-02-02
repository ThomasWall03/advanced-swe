package de.bilkewall.plugins.api

import de.bilkewall.application.api.APIWrapperInterface
import de.bilkewall.domain.AppCategory
import de.bilkewall.domain.AppDrink
import de.bilkewall.plugins.api.dto.DrinksCategoryDto
import de.bilkewall.plugins.api.dto.DrinksDto
import de.bilkewall.plugins.util.toAppDrink
import io.ktor.client.call.body
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class APIWrapper(val apiManager: APIManager) : APIWrapperInterface {
    override suspend fun getDrinkById(id: Int): List<AppDrink> {
        val drinksDto: DrinksDto = apiManager.get("lookup.php?i=${id}").body()
        return drinksDto.drinks?.map { it.toAppDrink() } ?: emptyList()
    }

    override suspend fun getDrinksByFirstLetter(firstLetter: Char): List<AppDrink> {
        val drinksDto: DrinksDto = apiManager.get("search.php?f=$firstLetter").body()
        return drinksDto.drinks?.map { it.toAppDrink() } ?: emptyList()
    }

    override suspend fun getDrinksByName(name: String): List<AppDrink> {
        val drinksDto: DrinksDto = apiManager.get("search.php?s=$name").body()
        return drinksDto.drinks?.map { it.toAppDrink() } ?: emptyList()
    }

    override suspend fun getAllDrinksAtoZ(): List<AppDrink> = coroutineScope {
        val deferredDrinks = ('a'..'z').map { letter ->
            async { getDrinksByFirstLetter(letter) }
        }
        deferredDrinks.awaitAll().flatten()
    }

    override suspend fun getDrinkCategories(): List<AppCategory> {
        val drinksCategoryDto: DrinksCategoryDto = apiManager.get("list.php?c=list").body()
        return drinksCategoryDto.drinks?.map { AppCategory(it.strCategory) } ?: emptyList()
    }
}