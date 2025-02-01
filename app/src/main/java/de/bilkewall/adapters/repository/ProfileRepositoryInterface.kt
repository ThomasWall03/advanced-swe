package de.bilkewall.plugins.database.profile

import de.bilkewall.domain.AppProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepositoryInterface {
    val allProfiles: Flow<List<AppProfile>>
    val activeProfile: Flow<AppProfile>

    suspend fun insert(profile: AppProfile): Long

    suspend fun delete(profile: AppProfile)

    suspend fun deleteAllProfiles()

    suspend fun getProfileCount(): Int

    suspend fun deactivateActiveProfile()

    suspend fun setActiveProfile(profileId: Int)
}