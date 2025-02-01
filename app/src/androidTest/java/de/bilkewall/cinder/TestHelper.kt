package de.bilkewall.cinder

import android.content.Context
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText

fun ComposeContentTestRule.launchCinderApp(context: Context) {
    setContent {
        CinderApp(

        )
    }
}

fun ComposeContentTestRule.waitUntilNodeIsDisplayed(
    text: String,
    timeoutMillis: Long = 5000
) {
    this.waitUntil(timeoutMillis = timeoutMillis) {
        this.onAllNodesWithText(text)
            .fetchSemanticsNodes(atLeastOneRootRequired = true).isNotEmpty()
    }
}


fun ComposeContentTestRule.waitUntilNodeWithTestTagIsDisplayed(
    tag: String,
    timeoutMillis: Long = 5000
) {
    this.waitUntil(timeoutMillis = timeoutMillis) {
        this.onAllNodesWithTag(tag)
            .fetchSemanticsNodes(atLeastOneRootRequired = true).isNotEmpty()
    }
}
