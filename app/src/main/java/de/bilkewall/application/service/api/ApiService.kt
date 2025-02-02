package de.bilkewall.application.service.api

import de.bilkewall.application.api.APIWrapperInterface
import de.bilkewall.domain.AppCategory
import de.bilkewall.domain.AppDrink

class ApiService(private val apiWrapper: APIWrapperInterface) {

    suspend fun getDrinkById(id: Int): List<AppDrink> {
        return apiWrapper.getDrinkById(id)
    }

    suspend fun getDrinksByName(name: String): List<AppDrink> {
        return apiWrapper.getDrinksByName(name)
    }

    suspend fun getAllDrinksAtoZ(): List<AppDrink> {
        return apiWrapper.getAllDrinksAtoZ()
    }

    suspend fun getDrinkCategories(): List<AppCategory> {
        return apiWrapper.getDrinkCategories()
    }
}