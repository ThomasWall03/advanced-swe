package de.bilkewall.adapters.service

import de.bilkewall.plugins.api.APIManager
import de.bilkewall.plugins.dto.CategoryDto
import de.bilkewall.plugins.dto.DrinkDto
import de.bilkewall.plugins.dto.DrinksCategoryDto
import de.bilkewall.plugins.dto.DrinksDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class DrinkService {

    private val apiManager = APIManager()

    suspend fun getDrinkById(id: Int): List<DrinkDto> {
        val drinksDto: DrinksDto = apiManager.jsonHTTPClient.get("lookup.php?i=${id}").body()
        return drinksDto.drinks?: emptyList()
    }

    suspend fun getDrinksByFirstLetter(firstLetter: Char): List<DrinkDto> {
        val drinksDto: DrinksDto = apiManager.jsonHTTPClient.get("search.php?f=$firstLetter").body()
        return drinksDto.drinks ?: emptyList()
    }

    suspend fun getDrinksByName(name: String): List<DrinkDto> {
        val drinksDto: DrinksDto = apiManager.jsonHTTPClient.get("search.php?s=$name").body()
        return drinksDto.drinks ?: emptyList()
    }

    suspend fun getAllDrinksAtoZ(): List<DrinkDto> = coroutineScope {
        val deferredDrinks = ('a'..'z').map { letter ->
            async { getDrinksByFirstLetter(letter) }
        }
        deferredDrinks.awaitAll().flatten()
    }

    suspend fun getDrinkById(id: String): DrinkDto {
        val drinksDto: DrinksDto = apiManager.jsonHTTPClient.get("lookup.php?i=$id").body()
        return drinksDto.drinks?.get(0)?: DrinkDto("","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","")
    }

    suspend fun getDrinkCategories(): List<CategoryDto> {
        val drinksCategoryDto: DrinksCategoryDto = apiManager.jsonHTTPClient.get("list.php?c=list").body()
        return drinksCategoryDto.drinks ?: emptyList()
    }
}