package de.bilkewall.plugins.database.match

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Insert
    suspend fun insert(match: Match)

    @Query("SELECT * FROM match_table")
    fun getAllMatches(): Flow<List<Match>>

    @Delete
    suspend fun delete(match: Match)

    @Query("DELETE FROM match_table")
    suspend fun deleteAllMatches()

    @Query("SELECT * FROM match_table WHERE profileId = :profileId")
    fun getAllMatchesForProfileId(profileId: Int): List<Match>

    @Query("SELECT * FROM match_table WHERE profileId = :profileId AND outcome = 1")
    fun getAllPositiveMatchesForProfileId(profileId: Int): List<Match>

    @Query("DELETE FROM match_table WHERE profileId = :profileId")
    fun deleteMatchesForProfile(profileId: Int)

}