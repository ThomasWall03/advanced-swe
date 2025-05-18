package de.bilkewall.domain.refactor

import de.bilkewall.domain.Profile

class RefactorBreakMain {
    fun getProfileDisplayInfo(profile: Profile): String {
        return profile.getDisplayInfo()
    }
}