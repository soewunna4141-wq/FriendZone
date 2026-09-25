package com.fz.friendzone.feature.news

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
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
    fun createPost_withCurrentProfile_enablesPostButton() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        val profileRepository = object : ProfileRepository {
            override fun getProfile(): Profile? {
                return profile
            }

            override fun getProfile(userId: String): Profile? {
                return profile.takeIf { it.userId == userId }
            }

            override fun saveProfile(profile: Profile) {
            }
        }

        val newsRepository = object : NewsRepository {
            override fun getPosts(): List<Post> {
                return emptyList()
            }

            override fun savePost(post: Post) {
            }
        }

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository
            )
        }

        composeTestRule.onNodeWithText(
            "What is on your mind?"
        ).assertIsEnabled()

        composeTestRule.onNodeWithText(
            "Post"
        ).assertIsNotEnabled()

        composeTestRule.onNodeWithText(
            "What is on your mind?"
        ).performTextInput("Hello FriendZone")

        composeTestRule.onNodeWithText(
            "Post"
        ).assertIsEnabled()
    }

    @Test
    fun createPost_withoutCurrentProfile_disablesPostInputAndButton() {
        val profileRepository = object : ProfileRepository {
            override fun getProfile(): Profile? {
                return null
            }

            override fun getProfile(userId: String): Profile? {
                return null
            }

            override fun saveProfile(profile: Profile) {
            }
        }

        val newsRepository = object : NewsRepository {
            override fun getPosts(): List<Post> {
                return emptyList()
            }

            override fun savePost(post: Post) {
            }
        }

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository
            )
        }

        composeTestRule.onNodeWithText(
            "What is on your mind?"
        ).assertIsNotEnabled()

        composeTestRule.onNodeWithText(
            "Post"
        ).assertIsNotEnabled()
    }

    @Test
    fun createPost_withCurrentProfile_savesPostWithProfileUserId() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        var savedPost: Post? = null

        val profileRepository = object : ProfileRepository {
            override fun getProfile(): Profile? {
                return profile
            }

            override fun getProfile(userId: String): Profile? {
                return profile.takeIf { it.userId == userId }
            }

            override fun saveProfile(profile: Profile) {
            }
        }

        val newsRepository = object : NewsRepository {
            override fun getPosts(): List<Post> {
                return savedPost?.let { listOf(it) } ?: emptyList()
            }

            override fun savePost(post: Post) {
                savedPost = post
            }
        }

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository
            )
        }

        composeTestRule.onNodeWithText(
            "What is on your mind?"
        ).performTextInput("Hello FriendZone")

        composeTestRule.onNodeWithText(
            "Post"
        ).performClick()

        composeTestRule.runOnIdle {
            check(savedPost?.userId == "user-1")
            check(savedPost?.caption == "Hello FriendZone")
        }
    }
}
