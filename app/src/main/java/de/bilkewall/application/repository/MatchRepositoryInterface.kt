package de.bilkewall.application.repository

import de.bilkewall.domain.Match

interface MatchRepositoryInterface {
    suspend fun insert(match: Match)

    fun getAllMatchesForCurrentProfile(profileId: Int): List<Match>

    fun deleteMatchesForProfile(profileId: Int)
}
