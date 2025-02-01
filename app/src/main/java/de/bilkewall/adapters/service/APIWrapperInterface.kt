package de.bilkewall.adapters.service

import de.bilkewall.domain.AppCategory
import de.bilkewall.domain.AppDrink

interface APIWrapperInterface {
    suspend fun getDrinkById(id: Int): List<AppDrink>
    suspend fun getDrinksByFirstLetter(firstLetter: Char): List<AppDrink>
    suspend fun getDrinksByName(name: String): List<AppDrink>
    suspend fun getAllDrinksAtoZ(): List<AppDrink>
    suspend fun getDrinkCategories(): List<AppCategory>
}