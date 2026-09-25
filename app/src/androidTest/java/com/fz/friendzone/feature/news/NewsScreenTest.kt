package com.fz.friendzone.feature.news

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.fz.friendzone.core.model.Post
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

            override fun saveProfile(profile: com.fz.friendzone.core.model.Profile) {
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

            override fun saveProfile(profile: com.fz.friendzone.core.model.Profile) {
            }
        }

        composeTestRule.setContent {
            NewsScreen(
                repository = repository,
                profileRepository = profileRepository
            )
        }

        composeTestRule.onNodeWithText(
            com.fz.friendzone.R.string.news_no_posts
        )
    }
}
