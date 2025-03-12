package de.bilkewall.plugins.database.profile

import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.domain.Profile
import de.bilkewall.plugins.util.toProfile
import de.bilkewall.plugins.util.toProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ProfileRepository(
    private val profileDao: ProfileDao,
) : ProfileRepositoryInterface {
    override val allProfiles: Flow<List<Profile>> =
        profileDao.getAllProfiles().map { profileEntities -> profileEntities.map { it.toProfile() } }
    override val activeProfile: Flow<Profile?> =
        profileDao.getActiveProfile().map { it?.toProfile() }

    override suspend fun insert(profile: Profile): Long = profileDao.insert(profile.toProfileEntity())

    override suspend fun delete(profile: Profile) {
        profileDao.delete(profile.toProfileEntity())

        if (profile.isActiveProfile) {
            setActiveProfile(allProfiles.first().first().profileId)
        }
    }

    override suspend fun getProfileCount(): Int = profileDao.getProfileCount()

    override suspend fun deactivateActiveProfile() {
        profileDao.deactivateActiveProfile()
    }

    override suspend fun setActiveProfile(profileId: Int) {
        profileDao.setActiveProfile(profileId)
    }
}
