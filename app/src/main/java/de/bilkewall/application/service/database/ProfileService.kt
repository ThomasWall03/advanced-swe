package de.bilkewall.application.service.database

import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.domain.AppProfile
import kotlinx.coroutines.flow.Flow

class ProfileService(
    private var profileRepository: ProfileRepositoryInterface,
) {
    fun getActiveProfile(): Flow<AppProfile?> {
        return profileRepository.activeProfile
    }
}