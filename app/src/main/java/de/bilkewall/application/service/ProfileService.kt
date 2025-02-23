package de.bilkewall.application.service

import de.bilkewall.application.repository.MatchRepositoryInterface
import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.application.repository.SharedFilterRepositoryInterface
import de.bilkewall.domain.DrinkTypeFilter
import de.bilkewall.domain.IngredientValueFilter
import de.bilkewall.domain.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ProfileService private constructor(
    private var profileRepository: ProfileRepositoryInterface,
    private val sharedFilterRepository: SharedFilterRepositoryInterface,
    private val matchRepository: MatchRepositoryInterface
) {

    companion object {
        @Volatile
        private var instance: ProfileService? = null

        fun getInstance(
            profileRepository: ProfileRepositoryInterface,
            sharedFilterRepository: SharedFilterRepositoryInterface,
            matchRepository: MatchRepositoryInterface
        ): ProfileService {
            return instance ?: synchronized(this) {
                instance ?: ProfileService(profileRepository, sharedFilterRepository, matchRepository).also {
                    instance = it
                }
            }
        }
    }

    val allProfiles: Flow<List<Profile>> by lazy { profileRepository.allProfiles }

    fun getActiveProfile(): Flow<Profile?> {
        return profileRepository.activeProfile
    }

    suspend fun saveProfile(profileName: String, selectedDrinkTypeOptions: List<String>, selectedIngredientOptions: List<String>) {
        profileRepository.deactivateActiveProfile()
        val id =
            profileRepository.insert(Profile(profileName = profileName, isActiveProfile = true))

        selectedDrinkTypeOptions.forEach { filter ->
            sharedFilterRepository.insertDrinkTypeFilter(DrinkTypeFilter(filter, id.toInt()))
        }

        selectedIngredientOptions.forEach { filter ->
            sharedFilterRepository.insertIngredientValueFilter(
                IngredientValueFilter(
                    filter,
                    id.toInt()
                )
            )
        }
    }

    suspend fun deleteProfile(profile: Profile){
        profileRepository.delete(profile)
        sharedFilterRepository.deleteIngredientValueFiltersByProfileId(profile.profileId)
        sharedFilterRepository.deleteDrinkTypeFiltersByProfileId(profile.profileId)
        matchRepository.deleteMatchesForProfile(profile.profileId)

        if (profile.isActiveProfile) {
            if(allProfiles.first().isNotEmpty()) {
                setCurrentProfile(allProfiles.first().first())
            }
        }
    }

    suspend fun setCurrentProfile(profile: Profile) {
        profileRepository.deactivateActiveProfile()
        profileRepository.setActiveProfile(profile.profileId)
    }

    suspend fun checkIfProfilesExist() = profileRepository.getProfileCount() > 0
}