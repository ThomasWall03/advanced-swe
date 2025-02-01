package de.bilkewall.cinder.database.profile

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "profile_table")
data class Profile(
    @PrimaryKey(autoGenerate = true) val profileId: Int = 0,
    val profileName: String,
    val isActiveProfile: Boolean = false,
    val creationDate: Long = System.currentTimeMillis(),
    val creationDateString: String = LocalDateTime.now().toString()
)
