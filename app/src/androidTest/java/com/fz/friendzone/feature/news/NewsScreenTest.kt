package com.fz.friendzone.feature.news

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.ProfileRepository
import org.junit.Rule
import org.junit.Test

class NewsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun newsScreen_displaysPostCaption() {
        val repository = object : NewsRepository {
            override fun getPosts(): List<Post> {
                return listOf(
                    Post(
                        id = "post-1",
                        userId = "user-1",
                        caption = "Test post"
                    )
                )
            }
        }

        val profileRepository = object : ProfileRepository {
            override fun getProfile() = null

            override fun saveProfile(profile: Profile) {
            }
        }

        composeTestRule.setContent {
            NewsScreen(
                repository = repository,
                profileRepository = profileRepository
            )
        }

        composeTestRule.onNodeWithText("Test post")
            .assertIsDisplayed()
    }

    @Test
    fun newsScreen_displaysEmptyStateWhenThereAreNoPosts() {
        val repository = object : NewsRepository {
            override fun getPosts(): List<Post> {
                return emptyList()
            }
        }

        val profileRepository = object : ProfileRepository {
            override fun getProfile() = null

            override fun saveProfile(profile: Profile) {
            }
        }

        composeTestRule.setContent {
            NewsScreen(
                repository = repository,
                profileRepository = profileRepository
            )
        }

        composeTestRule.onNodeWithText("No posts yet")
            .assertIsDisplayed()
    }

    @Test
    fun newsScreen_displaysProfileDisplayName() {
        val repository = object : NewsRepository {
            override fun getPosts(): List<Post> {
                return listOf(
                    Post(
                        id = "post-1",
                        userId = "user-1",
                        caption = "Profile test post"
                    )
                )
            }
        }

        val profileRepository = object : ProfileRepository {
            private val profile = Profile(
                userId = "user-1",
                displayName = "Test User"
            )

            override fun getProfile(): Profile? {
                return profile
            }

            override fun getProfile(userId: String): Profile? {
                return profile.takeIf { it.userId == userId }
            }

            override fun saveProfile(profile: Profile) {
            }
        }

        composeTestRule.setContent {
            NewsScreen(
                repository = repository,
                profileRepository = profileRepository
            )
        }

        composeTestRule.onNodeWithText("Test User")
            .assertIsDisplayed()
    }
}
