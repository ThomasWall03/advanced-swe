package de.bilkewall.plugins.database.match

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Insert
    suspend fun insert(match: MatchEntity)

    @Query("SELECT * FROM match_table")
    fun getAllMatches(): Flow<List<MatchEntity>>

    @Delete
    suspend fun delete(match: MatchEntity)

    @Query("DELETE FROM match_table")
    suspend fun deleteAllMatches()

    @Query("SELECT * FROM match_table WHERE profileId = :profileId")
    fun getAllMatchesForProfileId(profileId: Int): List<MatchEntity>

    @Query("SELECT * FROM match_table WHERE profileId = :profileId AND outcome = 1")
    fun getAllPositiveMatchesForProfileId(profileId: Int): List<MatchEntity>

    @Query("DELETE FROM match_table WHERE profileId = :profileId")
    fun deleteMatchesForProfile(profileId: Int)
}
