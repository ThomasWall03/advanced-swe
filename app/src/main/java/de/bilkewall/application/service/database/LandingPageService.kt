package de.bilkewall.application.service.database

import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.DrinkRepositoryInterface
import de.bilkewall.domain.AppDrink
import de.bilkewall.domain.AppDrinkIngredientCrossRef
import de.bilkewall.main.di.DependencyProvider.profileRepository
import kotlinx.coroutines.flow.first

class LandingPageService(
    private val drinkRepository: DrinkRepositoryInterface,
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface,
) {
    private val allDrinks = drinkRepository.getAllDrinks()

    suspend fun clearExistingData() {
        drinkRepository.deleteAllDrinks()
        drinkIngredientCrossRefRepository.deleteAllRelations()
    }

    suspend fun insertInitialData(allDrinksAPI: List<AppDrink>) {
        allDrinksAPI.forEach { drink ->
            if (allDrinks.first().map { it.drinkId }.contains(drink.drinkId)) {
                drinkRepository.update(drink)
                drinkIngredientCrossRefRepository.deleteAllRelationsOfADrink(drink.drinkId)
            } else {
                drinkRepository.insert(drink)
            }

            var relationInsertionIndex = 0
            drink.ingredients.forEach { ingredient ->
                if (!drink.ingredients.subList(0, relationInsertionIndex).map { ingredient }
                        .contains(ingredient)) {
                    drinkIngredientCrossRefRepository.insert(
                        AppDrinkIngredientCrossRef(
                            drink.drinkId,
                            ingredient,
                            drink.measurements[relationInsertionIndex]
                        )
                    )
                }
                relationInsertionIndex++
            }
        }
    }

    suspend fun checkIfProfilesExist() = profileRepository.getProfileCount() > 0
}