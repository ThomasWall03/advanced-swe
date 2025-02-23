package de.bilkewall.application.service

import de.bilkewall.application.repository.MatchRepositoryInterface
import de.bilkewall.domain.Match
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


class MatchServiceTest {
    //Mocking
    private val matchRepository: MatchRepositoryInterface = mock()

    private lateinit var matchService: MatchService

    @Before
    fun setUp() {
        val field = MatchService::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)

        matchService = MatchService.getInstance(matchRepository)
    }

    @Test
    fun `getInstance returns singleton instance`() {
        val instance1 = MatchService.getInstance(matchRepository)
        val instance2 = MatchService.getInstance(matchRepository)

        assertSame(instance1, instance2, "getInstance should return the same instance")
    }

    @Test
    fun `insert calls repository to insert match`() = runTest {
        val match = Match(drinkId = 1, profileId = 1, outcome = true)

        matchService.insert(match)

        verify(matchRepository).insert(match)
    }

    @Test
    fun `getMatchesForProfile returns matches for given profile`() = runTest {
        val profileId = 1
        val matches = listOf(
            Match(drinkId = 1, profileId = profileId, outcome = true),
            Match(drinkId = 2, profileId = profileId, outcome = true)
        )
        whenever(matchRepository.getAllMatchesForCurrentProfile(profileId)).thenReturn(matches)

        val result = matchService.getMatchesForProfile(profileId)

        assertEquals(matches, result)
    }
}