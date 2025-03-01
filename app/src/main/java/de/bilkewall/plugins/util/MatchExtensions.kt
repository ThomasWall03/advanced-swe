package de.bilkewall.plugins.util

import de.bilkewall.domain.Match
import de.bilkewall.plugins.database.match.MatchEntity

fun MatchEntity.toMatch(): Match =
    Match(
        drinkId = drinkId,
        profileId = profileId,
        outcome = outcome,
    )

fun Match.toMatchEntity(): MatchEntity =
    MatchEntity(
        drinkId = drinkId,
        profileId = profileId,
        outcome = outcome,
    )
