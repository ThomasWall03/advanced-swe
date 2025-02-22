package de.bilkewall.application.service

import de.bilkewall.application.repository.MatchRepositoryInterface
import de.bilkewall.domain.Match

class MatchService private constructor(
    private val matchRepository: MatchRepositoryInterface
) {

    companion object {
        @Volatile
        private var instance: MatchService? = null

        fun getInstance(
            matchRepository: MatchRepositoryInterface
        ): MatchService {
            return instance ?: synchronized(this) {
                instance ?: MatchService(matchRepository).also {
                    instance = it
                }
            }
        }
    }

    suspend fun insert(appMatch: Match) = matchRepository.insert(appMatch)

    suspend fun getMatchesForProfile(profileId: Int) = matchRepository.getAllMatchesForCurrentProfile(profileId)
}