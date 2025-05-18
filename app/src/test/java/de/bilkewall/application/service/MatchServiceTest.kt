package de.bilkewall.application.service

import de.bilkewall.application.repository.MatchRepositoryInterface
import de.bilkewall.domain.Match
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class MatchServiceTest {
    // Mocking
    private val matchRepository: MatchRepositoryInterface = mock()

    private fun createServiceInstance(): MatchService {
        val field = MatchService::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        return MatchService.getInstance(matchRepository)
    }

    @Test
    fun `getInstance returns singleton instance`() {
        // Arrange + Act
        val instance1 = MatchService.getInstance(matchRepository)
        val instance2 = MatchService.getInstance(matchRepository)

        // Assert
        assertSame(instance1, instance2, "getInstance should return the same instance")
    }

    @Test
    fun `insert calls repository to insert match`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val givenMatch = Match(drinkId = 1, profileId = 1, outcome = true)
        whenever(matchRepository.insert(givenMatch)).thenReturn(Unit)

        // Act
        target.insert(givenMatch)

        // Assert
        verify(matchRepository).insert(givenMatch)
    }

    @Test
    fun `getMatchesForProfile returns matches for given profile`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val givenProfileId = 1
        val expected = listOf(
            Match(drinkId = 1, profileId = givenProfileId, outcome = true),
            Match(drinkId = 2, profileId = givenProfileId, outcome = true),
        )
        whenever(matchRepository.getAllMatchesForCurrentProfile(givenProfileId)).thenReturn(expected)

        // Act
        val actual = target.getMatchesForProfile(givenProfileId)

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `deleteMatchesForProfile deletes matches for profile`() = runTest {
        // Arrange
        val target = createServiceInstance()
        val givenProfileId = 1

        // Act
        target.deleteMatchesForProfile(givenProfileId)

        // Assert
        verify(matchRepository).deleteMatchesForProfile(givenProfileId)
    }
}
