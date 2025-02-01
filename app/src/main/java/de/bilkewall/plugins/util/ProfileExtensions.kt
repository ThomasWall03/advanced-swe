package de.bilkewall.plugins.util

import de.bilkewall.domain.AppProfile
import de.bilkewall.plugins.database.profile.Profile

fun Profile.toAppProfile(): AppProfile {
    return AppProfile(
        profileId = profileId,
        profileName = profileName,
        isActiveProfile = isActiveProfile,
        creationDate = creationDate,
        creationDateString = creationDateString
    )
}

fun AppProfile.toProfile(): Profile {
    return Profile(
        profileId = profileId,
        profileName = profileName,
        isActiveProfile = isActiveProfile,
        creationDate = creationDate,
        creationDateString = creationDateString
    )
}