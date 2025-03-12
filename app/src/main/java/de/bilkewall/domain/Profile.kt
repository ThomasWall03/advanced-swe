package de.bilkewall.domain

import java.time.LocalDateTime

open class Profile(
    open val profileId: Int = 0,
    open val profileName: String,
    open val isActiveProfile: Boolean = false,
    open val creationDate: Long = System.currentTimeMillis(),
    open val creationDateString: String = LocalDateTime.now().toString(),
)
