package com.fz.friendzone.feature.news

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.data.repository.NewsRepository
import org.junit.Rule
import org.junit.Test

class NewsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyNewsState_showsNoPostsMessage() {
        val repository = object : NewsRepository {
            override fun getPosts(): List<Post> {
                return emptyList()
            }
        }

        composeTestRule.setContent {
            NewsScreen(
                repository = repository
            )
        }

        composeTestRule
            .onNodeWithText("No posts yet")
            .assertIsDisplayed()
    }

    @Test
    fun existingPosts_showsPostCaption() {
        val posts = listOf(
            Post(
                id = "post-1",
                userId = "user-1",
                caption = "Hello FriendZone"
            )
        )

        val repository = object : NewsRepository {
            override fun getPosts(): List<Post> {
                return posts
            }
        }

        composeTestRule.setContent {
            NewsScreen(
                repository = repository
            )
        }

        composeTestRule
            .onNodeWithText("Hello FriendZone")
            .assertIsDisplayed()
    }
}
