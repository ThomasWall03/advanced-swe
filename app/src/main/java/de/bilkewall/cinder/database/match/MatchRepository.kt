package de.bilkewall.cinder.database.match

import kotlinx.coroutines.flow.Flow

class MatchRepository (private val matchDao: MatchDao) {
    val allMatches = matchDao.getAllMatches()

    suspend fun insert(match: Match) {
        matchDao.insert(match)
    }

    suspend fun delete(match: Match){
       matchDao.delete(match)
    }

    fun getAllMatchesForCurrentProfile(profileId: Int): List<Match> {
        return matchDao.getAllMatchesForProfileId(profileId)
    }

    fun getAllPositiveMatchesForCurrentProfile(profileId: Int): List<Match> {
        return matchDao.getAllPositiveMatchesForProfileId(profileId)
    }

    fun deleteMatchesForProfile(profileId: Int){
        matchDao.deleteMatchesForProfile(profileId)
    }

}