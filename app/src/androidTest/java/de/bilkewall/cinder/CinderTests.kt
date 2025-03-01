package de.bilkewall.cinder

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToString
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CinderTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.launchCinderApp(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun app_launches() {
        println(composeTestRule.onRoot().printToString())
        composeTestRule.waitUntilNodeIsDisplayed("Create Profile", 30000)
        composeTestRule.onNodeWithText("Create Profile").assertExists()
        println(composeTestRule.onRoot(useUnmergedTree = true).printToString())
    }

    @Test
    fun profileCreationTest() { // Testfall 1
        // Wait on Database Population
        composeTestRule.waitUntilNodeIsDisplayed("Create Profile", 30000)

        // Test Naming Page
        composeTestRule.onNodeWithText("Create Profile").assertExists()
        composeTestRule.onNodeWithText("Next").assertIsNotEnabled()

        composeTestRule
            .onNodeWithText("Enter Profile Name")
            .performTextInput("Schnitzel")

        composeTestRule
            .onNodeWithText("Enter Profile Name")
            .assert(hasText("Schnitzel"))

        composeTestRule.onNodeWithText("Next").assertIsEnabled()
        composeTestRule.onNodeWithText("Next").performClick()

        // Test Drink Type Page
        composeTestRule.waitUntilNodeIsDisplayed("Cocktail")

        composeTestRule.onNodeWithText("Whats your type?").assertExists()
        composeTestRule
            .onNode(hasContentDescription("Back") and hasClickAction())
            .assertIsEnabled()
        composeTestRule.onNodeWithText("All").assertIsEnabled()
        composeTestRule.onNodeWithText("Next").assertIsNotEnabled()

        composeTestRule.onNodeWithText("Cocktail").performClick()
        composeTestRule.onNodeWithText("Next").assertIsNotEnabled()

        composeTestRule
            .onNodeWithText("Search")
            .performTextInput("Sho")

        composeTestRule.onNodeWithText("Shot").assertExists()
        composeTestRule.onNodeWithText("Shot").performClick()

        composeTestRule.onNodeWithText("Next").assertIsEnabled()

        // Test Back Rule to Name Setting Page
        composeTestRule
            .onNode(hasContentDescription("Back") and hasClickAction())
            .performClick()
        composeTestRule.waitUntilNodeIsDisplayed("Create Profile")

        composeTestRule
            .onNodeWithText("Enter Profile Name")
            .assert(hasText("Schnitzel"))
        composeTestRule.onNodeWithText("Next").performClick()

        composeTestRule.waitUntilNodeIsDisplayed("Cocktail")

        composeTestRule.onNodeWithText("Next").assertIsEnabled()
        composeTestRule.onNodeWithText("Next").performClick()

        // Test Ingredient Page
        composeTestRule.waitUntilNodeIsDisplayed("Vodka", 10000)
        composeTestRule.waitUntilNodeIsDisplayed("Advocaat", 10000)

        composeTestRule.onNodeWithText("What are you into?").assertExists()
        composeTestRule
            .onNode(hasContentDescription("Back") and hasClickAction())
            .assertIsEnabled()
        composeTestRule.onNodeWithText("All").assertIsEnabled()
        composeTestRule.onNodeWithText("Next").assertIsNotEnabled()

        // Filter specific ingredients
        composeTestRule.onNodeWithText("Advocaat").performClick()
        composeTestRule.onNodeWithText("Next").assertIsNotEnabled()

        composeTestRule.onNodeWithText("Amaretto").performClick()
        composeTestRule.onNodeWithText("Next").assertIsNotEnabled()

        composeTestRule.onNodeWithText("Apple").performClick()
        composeTestRule.onNodeWithText("Next").assertIsNotEnabled()

        composeTestRule.onNodeWithText("Apfelkorn").performClick()
        composeTestRule.onNodeWithText("Next").assertIsNotEnabled()

        composeTestRule
            .onNodeWithText("Search")
            .performTextInput("Vodk")

        composeTestRule.onNodeWithText("Vodka").assertExists()
        composeTestRule.onNodeWithText("Vodka").performClick()
        composeTestRule.onNodeWithText("Next").assertIsNotEnabled()

        composeTestRule.onNodeWithText("Absolut Vodka").performClick()
        composeTestRule.onNodeWithText("Next").assertIsEnabled()

        // Test Back Rule To Drink Type Page
        composeTestRule
            .onNode(hasContentDescription("Back") and hasClickAction())
            .performClick()
        composeTestRule.waitUntilNodeIsDisplayed("Whats your type?")

        composeTestRule.waitUntilNodeIsDisplayed("Cocktail")

        composeTestRule.onNodeWithText("Next").assertIsEnabled()
        composeTestRule.onNodeWithText("Next").performClick()

        composeTestRule.waitUntilNodeIsDisplayed("Vodka")
        composeTestRule.onNodeWithText("Next").performClick()

        // Test alcoholic filter page
        composeTestRule.waitUntilNodeIsDisplayed("Finish")

        composeTestRule.onNodeWithText("What's your kink?").assertExists()
        composeTestRule
            .onNode(hasContentDescription("Back") and hasClickAction())
            .assertIsEnabled()
        composeTestRule.onNodeWithText("Finish").assertIsEnabled()

        composeTestRule
            .onNode(withRole(Role.Checkbox))
            .performClick()
        composeTestRule.onNodeWithText("Finish").assertIsNotEnabled()

        composeTestRule
            .onNode(withRole(Role.Checkbox))
            .performClick()
        composeTestRule.onNodeWithText("Finish").assertIsEnabled()

        // Test Back Rule To Ingredient Filter Page
        composeTestRule
            .onNode(hasContentDescription("Back") and hasClickAction())
            .performClick()
        composeTestRule.waitUntilNodeIsDisplayed("What are you into?")

        composeTestRule.waitUntilNodeIsDisplayed("Vodka")

        composeTestRule.onNodeWithText("Next").assertIsEnabled()
        composeTestRule.onNodeWithText("Next").performClick()

        composeTestRule.waitUntilNodeIsDisplayed("What's your kink?")
        composeTestRule.onNodeWithText("Finish").performClick()
    }

    private fun withRole(role: Role) =
        SemanticsMatcher("${SemanticsProperties.Role.name} contains '$role'") {
            val roleProperty = it.config.getOrNull(SemanticsProperties.Role) ?: false
            roleProperty == role
        }
}
