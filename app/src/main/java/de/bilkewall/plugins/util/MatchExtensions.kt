package de.bilkewall.plugins.util

import de.bilkewall.domain.AppMatch
import de.bilkewall.plugins.database.match.Match

fun Match.toAppMatch(): AppMatch {
    return AppMatch(
        drinkId = drinkId,
        profileId = profileId,
        outcome = outcome
    )
}

fun AppMatch.toMatch(): Match {
    return Match(
        drinkId = drinkId,
        profileId = profileId,
        outcome = outcome
    )
}