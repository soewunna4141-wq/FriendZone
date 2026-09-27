package com.fz.friendzone.feature.profile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fz.friendzone.R
import com.fz.friendzone.app.FriendZoneDependencies
import com.fz.friendzone.core.model.Profile
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
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
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.profile_no_profile
                )
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.profile_create_demo
                )
            )
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
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.profile_create_demo
                )
            )
            .performClick()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.profile_display_name,
                    "Demo User"
                ),
                substring = true
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.profile_bio,
                    "Welcome to FriendZone"
                ),
                substring = true
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.profile_user_id,
                    "demo-user"
                ),
                substring = true
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.profile_reload
                )
            )
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
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.profile_display_name,
                    "Existing User"
                ),
                substring = true
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.profile_bio,
                    "Existing Bio"
                ),
                substring = true
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.profile_user_id,
                    "existing-user"
                ),
                substring = true
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.profile_reload
                )
            )
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
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.profile_reload
                )
            )
            .performClick()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.profile_display_name,
                    "Reload User"
                ),
                substring = true
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.profile_bio,
                    "Reload Bio"
                ),
                substring = true
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.profile_user_id,
                    "reload-user"
                ),
                substring = true
            )
            .assertIsDisplayed()
    }
}
