package de.bilkewall.plugins.database.match

import de.bilkewall.application.repository.MatchRepositoryInterface
import de.bilkewall.domain.Match
import de.bilkewall.plugins.util.toMatch
import de.bilkewall.plugins.util.toMatchEntity
import kotlinx.coroutines.flow.map

class MatchRepository(private val matchDao: MatchDao) : MatchRepositoryInterface {
    override val allMatches = matchDao.getAllMatches().map { it.map { it.toMatch() } }

    override suspend fun insert(match: Match) {
        matchDao.insert(match.toMatchEntity())
    }

    override suspend fun delete(match: Match) {
        matchDao.delete(match.toMatchEntity())
    }

    override fun getAllMatchesForCurrentProfile(profileId: Int): List<Match> {
        return matchDao.getAllMatchesForProfileId(profileId).map { it.toMatch() }
    }

    override fun getAllPositiveMatchesForCurrentProfile(profileId: Int): List<Match> {
        return matchDao.getAllPositiveMatchesForProfileId(profileId).map { it.toMatch() }
    }

    override fun deleteMatchesForProfile(profileId: Int) {
        matchDao.deleteMatchesForProfile(profileId)
    }
}