package de.bilkewall.application.service

import de.bilkewall.application.repository.MatchRepositoryInterface
import de.bilkewall.domain.Match

class MatchService (
    private val matchRepository: MatchRepositoryInterface
) {
    suspend fun insert(appMatch: Match) = matchRepository.insert(appMatch)

    suspend fun getMatchesForProfile(profileId: Int) = matchRepository.getAllMatchesForCurrentProfile(profileId)
}