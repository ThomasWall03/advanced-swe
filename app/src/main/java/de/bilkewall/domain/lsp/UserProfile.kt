package de.bilkewall.domain.lsp

import de.bilkewall.domain.Profile

data class UserProfile(
    override val profileId: Int = 0,
    override val profileName: String,
    override val isActiveProfile: Boolean = false,
    val email: String,
    val phoneNumber: String? = null
) : Profile(profileId, profileName, isActiveProfile) {
    fun getProfileInfo(): String {
        return "Email: $email" + (phoneNumber?.let { ", Phone: $it" } ?: "")
    }

    override fun getDisplayInfo(): String {
        return "Userprofil – Email: $email"
    }
}