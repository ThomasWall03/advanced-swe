package de.bilkewall.application.service

import de.bilkewall.application.repository.MatchRepositoryInterface
import de.bilkewall.domain.AppMatch

class MatchService (
    private val matchRepository: MatchRepositoryInterface
) {
    suspend fun insert(appMatch: AppMatch) = matchRepository.insert(appMatch)
}