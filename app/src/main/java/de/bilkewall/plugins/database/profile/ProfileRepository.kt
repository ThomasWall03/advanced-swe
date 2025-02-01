package de.bilkewall.plugins.database.profile

import de.bilkewall.adapters.repository.ProfileRepositoryInterface
import de.bilkewall.domain.AppProfile
import de.bilkewall.plugins.util.toAppProfile
import de.bilkewall.plugins.util.toProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ProfileRepository (private val profileDao: ProfileDao) : ProfileRepositoryInterface {
    override val allProfiles: Flow<List<AppProfile>> = profileDao.getAllProfiles().map { it.map { it.toAppProfile() } }
    override val activeProfile: Flow<AppProfile?> = profileDao.getActiveProfile().map { it?.toAppProfile() }

    override suspend fun insert(profile: AppProfile): Long {
        return profileDao.insert(profile.toProfile())
    }

    override suspend fun delete(profile: AppProfile) {
        profileDao.delete(profile.toProfile())

        if (profile.isActiveProfile) {
            setActiveProfile(allProfiles.first().first().profileId)
        }
    }

    override suspend fun deleteAllProfiles() {
        profileDao.deleteAllProfiles()
    }

    override suspend fun getProfileCount(): Int {
        return profileDao.getProfileCount()
    }

    override suspend fun deactivateActiveProfile() {
        profileDao.deactivateActiveProfile()
    }

    override suspend fun setActiveProfile(profileId: Int) {
        profileDao.setActiveProfile(profileId)
    }
}