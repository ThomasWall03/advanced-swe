package de.bilkewall.application.service

import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.domain.Profile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.whenever

class ProfileManagementServiceTest {
    // Mocking
    private val profileRepository: ProfileRepositoryInterface = mock()

    private fun createServiceInstance(): ProfileManagementService {
        val field = ProfileManagementService::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        return ProfileManagementService.getInstance(profileRepository)
    }

    @Test
    fun `getInstance returns singleton instance`() {
        // Arrange + Act
        val instance1 = ProfileManagementService.getInstance(profileRepository)
        val instance2 = ProfileManagementService.getInstance(profileRepository)

        // Assert
        assertSame(instance1, instance2, "getInstance should return the same instance")
    }

    @Test
    fun `allProfiles returns flow of profiles`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val expected = mockProfiles
        whenever(profileRepository.allProfiles).thenReturn(flowOf(expected))

        // Act
        val actual = target.allProfiles.first()

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `getActiveProfile returns active profile`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val expected = mockProfile
        whenever(profileRepository.activeProfile).thenReturn(flowOf(expected))

        // Act
        val actual = target.getActiveProfile().first()

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `getActiveProfile returns null`() = runTest {
        // Arrange
        val target = createServiceInstance()
        whenever(profileRepository.activeProfile).thenReturn(flowOf(null))

        // Act
        val actual = target.getActiveProfile().first()

        // Assert
        assertNull(actual)
    }

    @Test
    fun `saveProfile creates profile and adds filters`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val givenProfileName = "TestProfile"
        val expectedId = 10
        whenever(profileRepository.insert(any())).thenReturn(expectedId.toLong())

        // Act
        val actual = target.saveProfile(givenProfileName)

        // Assert
        assertEquals(expectedId, actual)
        verify(profileRepository).deactivateActiveProfile()
        verify(profileRepository).insert(
            argThat { profile ->
                profile.profileName == givenProfileName && profile.isActiveProfile
            },
        )
    }

    @Test
    fun `deleteProfile removes profile and cleans up related data`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val givenProfile = mockProfile

        // Act
        target.deleteProfile(givenProfile)

        // Assert
        verify(profileRepository).delete(givenProfile)
    }

    @Test
    fun `setCurrentProfile deactivates current profile and sets new active profile`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val givenProfile = mockProfile

        // Act
        target.setCurrentProfile(givenProfile)

        // Assert
        verify(profileRepository).deactivateActiveProfile()
        verify(profileRepository).setActiveProfile(givenProfile.profileId)
    }

    @Test
    fun `checkIfProfilesExist returns true if profiles exist`() = runTest {
        // Arrange
        val target = createServiceInstance()
        whenever(profileRepository.getProfileCount()).thenReturn(1)

        // Act
        val actual = target.checkIfProfilesExist()

        // Assert
        assertTrue(actual)
    }

    @Test
    fun `checkIfProfilesExist returns false if no profiles exist`() = runTest {
        // Arrange
        val target = createServiceInstance()
        whenever(profileRepository.getProfileCount()).thenReturn(0)

        // Act
        val actual = target.checkIfProfilesExist()

        // Assert
        assertFalse(actual)
    }

    // Test Data
    private val mockProfiles =
        listOf(
            Profile(profileId = 1, profileName = "Profile1", isActiveProfile = true),
            Profile(profileId = 2, profileName = "Profile2", isActiveProfile = false),
        )
    private val mockProfile = Profile(profileId = 1, profileName = "Profile1", isActiveProfile = true)
}
