package de.bilkewall.plugins.util

import de.bilkewall.domain.Profile
import de.bilkewall.plugins.database.profile.ProfileEntity

fun ProfileEntity.toProfile(): Profile =
    Profile(
        profileId = profileId,
        profileName = profileName,
        isActiveProfile = isActiveProfile,
        creationDate = creationDate,
        creationDateString = creationDateString,
    )

fun Profile.toProfileEntity(): ProfileEntity =
    ProfileEntity(
        profileId = profileId,
        profileName = profileName,
        isActiveProfile = isActiveProfile,
        creationDate = creationDate,
        creationDateString = creationDateString,
    )
