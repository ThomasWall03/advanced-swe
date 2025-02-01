package de.bilkewall.adapters.repository

import de.bilkewall.domain.AppMatch
import kotlinx.coroutines.flow.Flow

interface MatchRepositoryInterface {
    val allMatches: Flow<List<AppMatch>>

    suspend fun insert(match: AppMatch)

    suspend fun delete(match: AppMatch)

    fun getAllMatchesForCurrentProfile(profileId: Int): List<AppMatch>

    fun getAllPositiveMatchesForCurrentProfile(profileId: Int): List<AppMatch>

    fun deleteMatchesForProfile(profileId: Int)
}