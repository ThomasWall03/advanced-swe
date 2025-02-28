package de.bilkewall.application.service

import de.bilkewall.application.repository.MatchRepositoryInterface
import de.bilkewall.application.repository.ProfileRepositoryInterface
import de.bilkewall.application.repository.SharedFilterRepositoryInterface
import de.bilkewall.domain.DrinkTypeFilter
import de.bilkewall.domain.IngredientFilter
import de.bilkewall.domain.Profile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.whenever

class ProfileManagementServiceTest {
    //Mocking
    private val profileRepository: ProfileRepositoryInterface = mock()
    private val sharedFilterRepository: SharedFilterRepositoryInterface = mock()
    private val matchRepository: MatchRepositoryInterface = mock()

    private lateinit var profileManagementService: ProfileManagementService

    @Before
    fun setup() {
        val field = ProfileManagementService::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        profileManagementService = ProfileManagementService.getInstance(profileRepository)
    }

    @Test
    fun `getInstance returns singleton instance`() {
        val instance1 = ProfileManagementService.getInstance(profileRepository)
        val instance2 = ProfileManagementService.getInstance(profileRepository)

        assertSame(instance1, instance2, "getInstance should return the same instance")
    }

    @Test
    fun `getActiveProfile returns active profile`() = runTest {
        val activeProfile = Profile(profileId = 1, profileName = "ActiveProfile", isActiveProfile = true)
        whenever(profileRepository.activeProfile).thenReturn(flowOf(activeProfile))

        val result = profileManagementService.getActiveProfile().first()

        assertEquals(activeProfile, result)
    }

    @Test
    fun `getActiveProfile returns null`() = runTest {
        whenever(profileRepository.activeProfile).thenReturn(flowOf(null))

        val result = profileManagementService.getActiveProfile().first()

        assertNull(result)
    }

    @Test
    fun `saveProfile creates profile and adds filters`() = runTest {
        val profileName = "TestProfile"
        val drinkFilters = listOf("Cocktail", "Mocktail")
        val ingredientFilters = listOf("Rum", "Lime")

        whenever(profileRepository.insert(any())).thenReturn(10)

        profileManagementService.saveProfile(profileName)


        verify(profileRepository).deactivateActiveProfile()
        verify(profileRepository).insert(argThat { profile ->
            profile.profileName == profileName && profile.isActiveProfile
        })
        verify(sharedFilterRepository).insertDrinkTypeFilter(DrinkTypeFilter("Cocktail", 10))
        verify(sharedFilterRepository).insertDrinkTypeFilter(DrinkTypeFilter("Mocktail", 10))
        verify(sharedFilterRepository).insertIngredientFilter(IngredientFilter("Rum", 10))
        verify(sharedFilterRepository).insertIngredientFilter(IngredientFilter("Lime", 10))
    }

    @Test
    fun `deleteProfile removes profile and cleans up related data`() = runTest {
        val profile = Profile(profileId = 1, profileName = "Test", isActiveProfile = true)
        whenever(profileRepository.allProfiles).thenReturn(
            flowOf(
                listOf(
                    Profile(profileId = 1, profileName = "Test1", isActiveProfile = true),
                    Profile(profileId = 2, profileName = "Test2", isActiveProfile = false)
                )
            )
        )

        profileManagementService.deleteProfile(profile)

        verify(profileRepository).delete(profile)
        verify(sharedFilterRepository).deleteIngredientValueFiltersByProfileId(1)
        verify(sharedFilterRepository).deleteDrinkTypeFiltersByProfileId(1)
        verify(matchRepository).deleteMatchesForProfile(1)
        verify(profileRepository).deactivateActiveProfile()
        verify(profileRepository).setActiveProfile(profile.profileId)
    }

    @Test
    fun `deleteProfile removes profile and cleans up related data, but is not active profile`() = runTest {
        val profile = Profile(profileId = 1, profileName = "Test", isActiveProfile = false)
        whenever(profileRepository.allProfiles).thenReturn(
            flowOf(
                listOf(
                    Profile(profileId = 1, profileName = "Test1", isActiveProfile = false),
                    Profile(profileId = 2, profileName = "Test2", isActiveProfile = true)
                )
            )
        )

        profileManagementService.deleteProfile(profile)

        verify(profileRepository).delete(profile)
        verify(sharedFilterRepository).deleteIngredientValueFiltersByProfileId(1)
        verify(sharedFilterRepository).deleteDrinkTypeFiltersByProfileId(1)
        verify(matchRepository).deleteMatchesForProfile(1)
        verify(profileRepository, times(0)).deactivateActiveProfile()
        verify(profileRepository, times(0)).setActiveProfile(profile.profileId)
    }
    @Test
    fun `deleteProfile removes profile and cleans up related data, but allProfiles is empty`() = runTest {
        val profile = Profile(profileId = 1, profileName = "Test", isActiveProfile = false)
        whenever(profileRepository.allProfiles).thenReturn(
            flowOf(
                emptyList()
            )
        )

        profileManagementService.deleteProfile(profile)

        verify(profileRepository).delete(profile)
        verify(sharedFilterRepository).deleteIngredientValueFiltersByProfileId(1)
        verify(sharedFilterRepository).deleteDrinkTypeFiltersByProfileId(1)
        verify(matchRepository).deleteMatchesForProfile(1)
        verify(profileRepository, times(0)).deactivateActiveProfile()
        verify(profileRepository, times(0)).setActiveProfile(profile.profileId)
    }

    @Test
    fun `setCurrentProfile deactivates current profile and sets new active profile`() = runTest {
        val profile = Profile(profileId = 1, profileName = "TestProfile", isActiveProfile = true)

        profileManagementService.setCurrentProfile(profile)

        verify(profileRepository).deactivateActiveProfile()
        verify(profileRepository).setActiveProfile(profile.profileId)
    }

    @Test
    fun `checkIfProfilesExist returns true if profiles exist`() = runTest {
        whenever(profileRepository.getProfileCount()).thenReturn(1)

        val result = profileManagementService.checkIfProfilesExist()

        assertTrue(result)
    }

    @Test
    fun `checkIfProfilesExist returns false if no profiles exist`() = runTest {
        whenever(profileRepository.getProfileCount()).thenReturn(0)

        val result = profileManagementService.checkIfProfilesExist()

        assertFalse(result)
    }
}