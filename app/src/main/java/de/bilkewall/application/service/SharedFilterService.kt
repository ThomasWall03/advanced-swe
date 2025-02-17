package de.bilkewall.application.service

import de.bilkewall.application.repository.SharedFilterRepositoryInterface

class SharedFilterService(
    private val sharedFilterRepository: SharedFilterRepositoryInterface
) {
    suspend fun getIngredientFilterValues(profileId: Int) = sharedFilterRepository.getIngredientValueFiltersByProfileId(profileId)

    suspend fun getDrinkTypeFilterValues(profileId: Int) = sharedFilterRepository.getDrinkTypeFiltersByProfileId(profileId)
}