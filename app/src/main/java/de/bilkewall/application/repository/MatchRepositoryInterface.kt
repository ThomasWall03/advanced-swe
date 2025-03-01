package de.bilkewall.application.repository

import de.bilkewall.domain.Match
import kotlinx.coroutines.flow.Flow

interface MatchRepositoryInterface {
    val allMatches: Flow<List<Match>>

    suspend fun insert(match: Match)

    suspend fun delete(match: Match)

    fun getAllMatchesForCurrentProfile(profileId: Int): List<Match>

    fun getAllPositiveMatchesForCurrentProfile(profileId: Int): List<Match>

    fun deleteMatchesForProfile(profileId: Int)
}
