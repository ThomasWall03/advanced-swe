package de.bilkewall.application.service.database

import de.bilkewall.application.repository.DrinkIngredientCrossRefInterface
import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.application.repository.SharedFilterRepositoryInterface
import de.bilkewall.domain.AppDrinkTypeFilter
import de.bilkewall.domain.AppIngredientValueFilter
import de.bilkewall.domain.AppProfile

class CreateProfileService (
    private val profileRepository: ProfileRepositoryInterface,
    private val sharedFilterRepository: SharedFilterRepositoryInterface,
    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefInterface,
) {
    suspend fun saveProfile(profileName: String, selectedDrinkTypeOptions: List<String>, selectedIngredientOptions: List<String>) {
        profileRepository.deactivateActiveProfile()
        val id =
            profileRepository.insert(AppProfile(profileName = profileName, isActiveProfile = true))

        selectedDrinkTypeOptions.forEach { filter ->
            sharedFilterRepository.insertDrinkTypeFilter(AppDrinkTypeFilter(filter, id.toInt()))
        }

        selectedIngredientOptions.forEach { filter ->
            sharedFilterRepository.insertIngredientValueFilter(
                AppIngredientValueFilter(
                    filter,
                    id.toInt()
                )
            )
        }
    }

    fun getAllIngredientsSortedByName() = drinkIngredientCrossRefRepository.getAllIngredientsSortedByName()
}