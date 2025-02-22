package de.bilkewall.domain

import java.time.LocalDateTime

data class Profile(
    val profileId: Int = 0,
    val profileName: String,
    val isActiveProfile: Boolean = false,
    val creationDate: Long = System.currentTimeMillis(),
    val creationDateString: String = LocalDateTime.now().toString()
)
