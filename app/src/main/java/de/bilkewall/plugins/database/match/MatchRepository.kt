package de.bilkewall.plugins.database.match

import de.bilkewall.adapters.repository.MatchRepositoryInterface
import de.bilkewall.domain.AppMatch
import de.bilkewall.plugins.util.toAppMatch
import de.bilkewall.plugins.util.toMatch
import kotlinx.coroutines.flow.map

class MatchRepository(private val matchDao: MatchDao) : MatchRepositoryInterface {
    override val allMatches = matchDao.getAllMatches().map { it.map { it.toAppMatch() } }

    override suspend fun insert(match: AppMatch) {
        matchDao.insert(match.toMatch())
    }

    override suspend fun delete(match: AppMatch) {
        matchDao.delete(match.toMatch())
    }

    override fun getAllMatchesForCurrentProfile(profileId: Int): List<AppMatch> {
        return matchDao.getAllMatchesForProfileId(profileId).map { it.toAppMatch() }
    }

    override fun getAllPositiveMatchesForCurrentProfile(profileId: Int): List<AppMatch> {
        return matchDao.getAllPositiveMatchesForProfileId(profileId).map { it.toAppMatch() }
    }

    override fun deleteMatchesForProfile(profileId: Int) {
        matchDao.deleteMatchesForProfile(profileId)
    }
}