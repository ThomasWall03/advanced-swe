package de.bilkewall.domain.refactor

import de.bilkewall.domain.Profile
import de.bilkewall.domain.lsp.GuestProfile
import de.bilkewall.domain.lsp.UserProfile

class RefactorBreakMain {
    fun getProfileDisplayInfo(profile: Profile): String {
        return when (profile) {
            is GuestProfile -> "Gast mit Session-ID: ${profile.sessionId}"
            is UserProfile -> profile.getProfileInfo()
            else -> "Unbekanntes Profil"
        }
    }
}