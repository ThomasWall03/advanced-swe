package de.bilkewall.application.service

import de.bilkewall.application.repository.SharedFilterRepositoryInterface
import de.bilkewall.domain.DrinkTypeFilter
import de.bilkewall.domain.IngredientFilter

class SharedFilterService private constructor (
    private val sharedFilterRepository: SharedFilterRepositoryInterface
) {

    companion object {
        @Volatile
        private var instance: SharedFilterService? = null

        fun getInstance(
            sharedFilterRepository: SharedFilterRepositoryInterface
        ): SharedFilterService {
            return instance ?: synchronized(this) {
                instance ?: SharedFilterService(sharedFilterRepository).also {
                    instance = it
                }
            }
        }
    }

    suspend fun getIngredientFilterValues(profileId: Int) = sharedFilterRepository.getIngredientFiltersByProfileId(profileId)

    suspend fun getDrinkTypeFilterValues(profileId: Int) = sharedFilterRepository.getDrinkTypeFiltersByProfileId(profileId)

    suspend fun saveFiltersForProfile(profileId: Int, selectedDrinkTypeOptions: List<String>, selectedIngredientOptions: List<String>) {
        selectedDrinkTypeOptions.forEach { filter ->
            sharedFilterRepository.insertDrinkTypeFilter(DrinkTypeFilter(filter, profileId))
        }

        selectedIngredientOptions.forEach { filter ->
            sharedFilterRepository.insertIngredientFilter(IngredientFilter(filter, profileId))
        }
    }

    suspend fun deleteFiltersForProfile(profileId: Int) {
        sharedFilterRepository.deleteIngredientValueFiltersByProfileId(profileId)
        sharedFilterRepository.deleteDrinkTypeFiltersByProfileId(profileId)
    }
}