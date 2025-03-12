package de.bilkewall.application.repository

import de.bilkewall.domain.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepositoryInterface {
    val allProfiles: Flow<List<Profile>>
    val activeProfile: Flow<Profile?>

    suspend fun insert(profile: Profile): Long

    suspend fun delete(profile: Profile)

    suspend fun getProfileCount(): Int

    suspend fun deactivateActiveProfile()

    suspend fun setActiveProfile(profileId: Int)
}
