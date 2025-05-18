package de.bilkewall.domain.lsp

import de.bilkewall.domain.Profile

data class GuestProfile(
    override val profileName: String = "Guest",
    val sessionId: String = "GUEST-${System.currentTimeMillis()}"
): Profile(profileName = profileName) {
    fun generateNewSession(): GuestProfile {
        return this.copy(sessionId = "GUEST-${System.currentTimeMillis()}")
    }

    override fun getDisplayInfo(): String {
        return "Gastprofil – Session ID: $sessionId"
    }
}