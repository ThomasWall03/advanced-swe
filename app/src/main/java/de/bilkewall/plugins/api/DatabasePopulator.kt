package de.bilkewall.plugins.api

import android.util.Log
import de.bilkewall.adapters.DatabasePopulator
import de.bilkewall.application.repository.CategoryRepositoryInterface
import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.domain.AppDrinkIngredientCrossRef
import kotlinx.coroutines.flow.first

class DatabasePopulator(
    private val drinkRepository: DrinkRepositoryInterface,
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface,
    private val categoryRepository: CategoryRepositoryInterface,
    private val apiWrapper: APIWrapper
) : DatabasePopulator {
    private val allDrinks = drinkRepository.getAllDrinks()

    override suspend fun clearExistingData() {
        drinkRepository.deleteAllDrinks()
        drinkIngredientCrossRefRepository.deleteAllRelations()
    }

    override suspend fun insertInitialData() {
        val allCategoriesAPI = apiWrapper.getDrinkCategories()
        Log.i("DatabasePopulator", "Inserting ${allCategoriesAPI.size} categories")
        allCategoriesAPI.forEach { category ->
            categoryRepository.insert(category)
        }

        val allDrinksAPI = apiWrapper.getAllDrinksAtoZ()

        allDrinksAPI.forEach { drink ->
            if (allDrinks.first().map { it.drinkId }.contains(drink.drinkId)) {
                drinkRepository.update(drink)
                drinkIngredientCrossRefRepository.deleteAllRelationsOfADrink(drink.drinkId)
            } else {
                drinkRepository.insert(drink)
            }

            var relationInsertionIndex = 0
            drink.ingredients.forEach { ingredient ->
                if (!drink.ingredients.subList(0, relationInsertionIndex).contains(ingredient) && ingredient != "") {

                    var measurement = ""
                    // Sometimes the API returns a list of measurements that is shorter than the list of ingredients
                    try {
                        measurement = drink.measurements[relationInsertionIndex]
                    } catch (e: Exception) {
                        Log.e(
                            "LandingPageService.insertInitialData",
                            "Error while getting the measurement for drink with DrinkId: ${drink.drinkId}"
                        )
                    }

                    drinkIngredientCrossRefRepository.insert(
                        AppDrinkIngredientCrossRef(
                            drink.drinkId,
                            ingredient,
                            measurement
                        )
                    )
                    relationInsertionIndex++
                }
            }
        }
    }
}