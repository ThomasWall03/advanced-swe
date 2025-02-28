package de.bilkewall.application.service

import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.domain.Profile
import kotlinx.coroutines.flow.Flow

class ProfileManagementService private constructor(
    private var profileRepository: ProfileRepositoryInterface
) {

    companion object {
        @Volatile
        private var instance: ProfileManagementService? = null

        fun getInstance(
            profileRepository: ProfileRepositoryInterface
        ): ProfileManagementService {
            return instance ?: synchronized(this) {
                instance ?: ProfileManagementService(profileRepository).also {
                    instance = it
                }
            }
        }
    }

    val allProfiles: Flow<List<Profile>> by lazy { profileRepository.allProfiles }

    fun getActiveProfile(): Flow<Profile?> {
        return profileRepository.activeProfile
    }

    suspend fun saveProfile(profileName: String): Int {
        profileRepository.deactivateActiveProfile()
        val id =
            profileRepository.insert(Profile(profileName = profileName, isActiveProfile = true))

        return id.toInt()
    }

    suspend fun deleteProfile(profile: Profile){
        profileRepository.delete(profile)
    }

    suspend fun setCurrentProfile(profile: Profile) {
        profileRepository.deactivateActiveProfile()
        profileRepository.setActiveProfile(profile.profileId)
    }

    suspend fun checkIfProfilesExist() = profileRepository.getProfileCount() > 0
}