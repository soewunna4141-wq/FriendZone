package com.fz.friendzone.feature.reels

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.fz.friendzone.data.local.InMemoryReelsLocalDataSource
import com.fz.friendzone.data.repository.ReelsRepositoryImpl
import org.junit.Rule
import org.junit.Test

class ReelsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun reelsScreen_displaysReelCount() {
        val repository = ReelsRepositoryImpl(
            InMemoryReelsLocalDataSource()
        )

        composeTestRule.setContent {
            ReelsScreen(repository = repository)
        }

        composeTestRule
            .onNodeWithText("Reels: 0")
            .assertIsDisplayed()
    }
}
