package de.bilkewall.application.service

import de.bilkewall.application.repository.MatchRepositoryInterface
import de.bilkewall.domain.Match

class MatchService private constructor(
    private val matchRepository: MatchRepositoryInterface,
) {
    companion object {
        @Volatile
        private var instance: MatchService? = null

        fun getInstance(matchRepository: MatchRepositoryInterface): MatchService =
            instance ?: synchronized(this) {
                instance ?: MatchService(matchRepository).also {
                    instance = it
                }
            }
    }

    suspend fun insert(match: Match) = matchRepository.insert(match)

    fun getMatchesForProfile(profileId: Int) = matchRepository.getAllMatchesForCurrentProfile(profileId)

    fun deleteMatchesForProfile(profileId: Int) = matchRepository.deleteMatchesForProfile(profileId)
}
