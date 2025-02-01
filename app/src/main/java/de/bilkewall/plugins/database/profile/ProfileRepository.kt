package de.bilkewall.plugins.database.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ProfileRepository (private val profileDao: ProfileDao) {
    val allProfiles: Flow<List<Profile>> = profileDao.getAllProfiles()
    val activeProfile: Flow<Profile> = profileDao.getActiveProfile()

    suspend fun insert(profile: Profile): Long {
        return profileDao.insert(profile)
    }

    suspend fun delete(profile: Profile) {
        profileDao.delete(profile)

        if (profile.isActiveProfile) {
            setActiveProfile(allProfiles.first().first().profileId)
        }
    }

    suspend fun deleteAllProfiles() {
        profileDao.deleteAllProfiles()
    }

    suspend fun getProfileCount(): Int {
        return profileDao.getProfileCount()
    }

    suspend fun deactivateActiveProfile() {
        profileDao.deactivateActiveProfile()
    }

    suspend fun setActiveProfile(profileId: Int) {
        profileDao.setActiveProfile(profileId)
    }
}