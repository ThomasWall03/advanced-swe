package de.bilkewall.plugins.database.profile

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Insert
    suspend fun insert(profile: ProfileEntity): Long

    @Query("SELECT * FROM profile_table")
    fun getAllProfiles(): Flow<List<ProfileEntity>>

    @Delete
    suspend fun delete(profile: ProfileEntity)

    @Query("SELECT COUNT(*) FROM profile_table")
    suspend fun getProfileCount(): Int

    @Query("SELECT * FROM profile_table WHERE isActiveProfile = 1")
    fun getActiveProfile(): Flow<ProfileEntity?>

    @Query("UPDATE profile_table SET isActiveProfile = 0 WHERE isActiveProfile = 1")
    suspend fun deactivateActiveProfile()

    @Query("UPDATE profile_table SET isActiveProfile = 1 WHERE profileId = :profileId")
    suspend fun setActiveProfile(profileId: Int)
}
