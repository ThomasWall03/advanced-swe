package de.bilkewall.application.service

import de.bilkewall.application.repository.MatchRepositoryInterface
import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.application.repository.SharedFilterRepositoryInterface
import de.bilkewall.domain.AppDrinkTypeFilter
import de.bilkewall.domain.AppIngredientValueFilter
import de.bilkewall.domain.AppProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ProfileService(
    private var profileRepository: ProfileRepositoryInterface,
    private val sharedFilterRepository: SharedFilterRepositoryInterface,
    private val matchRepository: MatchRepositoryInterface
) {
    val allProfiles: Flow<List<AppProfile>> = profileRepository.allProfiles

    fun getActiveProfile(): Flow<AppProfile?> {
        return profileRepository.activeProfile
    }

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

    suspend fun deleteProfile(profile: AppProfile){
        profileRepository.delete(profile)
        sharedFilterRepository.deleteIngredientValueFiltersByProfileId(profile.profileId)
        sharedFilterRepository.deleteDrinkTypeFiltersByProfileId(profile.profileId)
        matchRepository.deleteMatchesForProfile(profile.profileId)

        if (profile.isActiveProfile) {
            setCurrentProfile(allProfiles.first().first())
        }
    }

    suspend fun setCurrentProfile(profile: AppProfile) {
        profileRepository.deactivateActiveProfile()
        profileRepository.setActiveProfile(profile.profileId)
    }

    suspend fun checkIfProfilesExist() = profileRepository.getProfileCount() > 0
}