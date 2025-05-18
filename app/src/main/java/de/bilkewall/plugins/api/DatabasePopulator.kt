package de.bilkewall.plugins.api

import android.util.Log
import de.bilkewall.adapters.DatabasePopulator
import de.bilkewall.application.repository.category.CategoryRepositoryManipulatorInterface
import de.bilkewall.application.repository.drink.DrinkRepositoryManipulatorInterface
import de.bilkewall.application.repository.drinkingredientcrossref.DrinkIngredientCrossRefManipulatorInterface
import de.bilkewall.domain.DrinkIngredientCrossRef
import kotlinx.coroutines.flow.first

class DatabasePopulator(
    private val drinkRepository: DrinkRepositoryManipulatorInterface,
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefManipulatorInterface,
    private val categoryRepository: CategoryRepositoryManipulatorInterface,
    private val apiWrapper: APIWrapper,
) : DatabasePopulator {
    private val allDrinks = drinkRepository.getAllDrinks()
    private val allCategories = categoryRepository.getAllCategories()

    override suspend fun clearExistingData() {
        drinkRepository.deleteAllDrinks()
        drinkIngredientCrossRefRepository.deleteAllRelations()
    }

    override suspend fun insertInitialData() {
        val allCategoriesAPI = apiWrapper.getDrinkCategories()
        allCategoriesAPI.forEach { category ->
            if (!allCategories.first().map { it.strCategory }.contains(category.strCategory)) {
                categoryRepository.insert(category)
            }
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
                            "Caught error while getting the measurement for drink with DrinkId: ${drink.drinkId}, because length of measurements (${drink.measurements.size}) < length of ingredients (${drink.ingredients.size})",
                        )
                    }

                    drinkIngredientCrossRefRepository.insert(
                        DrinkIngredientCrossRef(
                            drink.drinkId,
                            ingredient,
                            measurement,
                        ),
                    )
                    relationInsertionIndex++
                }
            }
        }
    }
}
