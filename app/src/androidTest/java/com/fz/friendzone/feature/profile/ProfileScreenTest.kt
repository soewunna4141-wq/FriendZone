package com.fz.friendzone.feature.profile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.fz.friendzone.app.FriendZoneDependencies
import com.fz.friendzone.core.model.Profile
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyProfileState_showsCreateProfileButton() {
        val dependencies = FriendZoneDependencies()

        composeTestRule.setContent {
            ProfileScreen(
                dependencies = dependencies
            )
        }

        composeTestRule
            .onNodeWithText("No profile")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Create Demo Profile")
            .assertIsDisplayed()
    }

    @Test
    fun createDemoProfile_showsProfileInformation() {
        val dependencies = FriendZoneDependencies()

        composeTestRule.setContent {
            ProfileScreen(
                dependencies = dependencies
            )
        }

        composeTestRule
            .onNodeWithText("Create Demo Profile")
            .performClick()

        composeTestRule
            .onNodeWithText("Display Name: Demo User")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Bio: Welcome to FriendZone")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("User ID: demo-user")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Reload Profile")
            .assertIsDisplayed()
    }

    @Test
    fun existingProfile_showsProfileInformation() {
        val dependencies = FriendZoneDependencies()

        val existingProfile = Profile(
            userId = "existing-user",
            displayName = "Existing User",
            bio = "Existing Bio"
        )

        dependencies.profileRepository.saveProfile(existingProfile)

        composeTestRule.setContent {
            ProfileScreen(
                dependencies = dependencies
            )
        }

        composeTestRule
            .onNodeWithText("Display Name: Existing User")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Bio: Existing Bio")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("User ID: existing-user")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Reload Profile")
            .assertIsDisplayed()
    }

    @Test
    fun reloadProfile_keepsProfileInformationDisplayed() {
        val dependencies = FriendZoneDependencies()

        val existingProfile = Profile(
            userId = "reload-user",
            displayName = "Reload User",
            bio = "Reload Bio"
        )

        dependencies.profileRepository.saveProfile(existingProfile)

        composeTestRule.setContent {
            ProfileScreen(
                dependencies = dependencies
            )
        }

        composeTestRule
            .onNodeWithText("Reload Profile")
            .performClick()

        composeTestRule
            .onNodeWithText("Display Name: Reload User")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Bio: Reload Bio")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("User ID: reload-user")
            .assertIsDisplayed()
    }
}
