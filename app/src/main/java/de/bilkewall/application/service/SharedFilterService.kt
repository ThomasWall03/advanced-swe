package de.bilkewall.application.service

import de.bilkewall.application.repository.SharedFilterRepositoryInterface

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

    suspend fun getIngredientFilterValues(profileId: Int) = sharedFilterRepository.getIngredientValueFiltersByProfileId(profileId)

    suspend fun getDrinkTypeFilterValues(profileId: Int) = sharedFilterRepository.getDrinkTypeFiltersByProfileId(profileId)
}