package com.fz.friendzone.feature.news

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.ProfileRepository
import org.junit.Assert.assertTrue
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
    fun emptyPosts_displaysNoPostsMessage() {
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
            "No posts yet"
        ).assertIsEnabled()
    }

    @Test
    fun errorState_displaysLoadErrorMessage() {
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
                error("Test load error")
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
            "Unable to load posts"
        ).assertIsEnabled()
    }

    @Test
    fun posts_withProfile_displayAuthorNameAndCaption() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Hello from FriendZone"
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
                return listOf(post)
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
            "Test User"
        ).assertIsEnabled()

        composeTestRule.onNodeWithText(
            "Hello from FriendZone"
        ).assertIsEnabled()
    }

    @Test
    fun posts_withProfile_withoutImage_displaysAvatarInitial() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Test User",
            profileImageUrl = null
        )

        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Hello from FriendZone"
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
                return listOf(post)
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
            "T"
        ).assertIsEnabled()
    }

    @Test
    fun posts_withoutProfile_displayCaption() {
        val post = Post(
            id = "post-1",
            userId = "missing-user",
            caption = "Post without profile"
        )

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
                return listOf(post)
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
            "Post without profile"
        ).assertIsEnabled()
    }

    @Test
    fun posts_renderInNewestToOlderOrder() {
        val newerPost = Post(
            id = "post-2",
            userId = "user-1",
            caption = "Newer post",
            createdAt = 2000L
        )

        val olderPost = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Older post",
            createdAt = 1000L
        )

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
                return listOf(newerPost, olderPost)
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

        val newerNode = composeTestRule
            .onAllNodesWithText("Newer post")
            .fetchSemanticsNodes()
            .single()

        val olderNode = composeTestRule
            .onAllNodesWithText("Older post")
            .fetchSemanticsNodes()
            .single()

        assertTrue(
            newerNode.boundsInRoot.top < olderNode.boundsInRoot.top
        )
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

    @Test
    fun createPost_withCurrentProfile_setsCreationTimestamp() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        var savedPost: Post? = null
        val beforeCreate = System.currentTimeMillis()

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
        ).performTextInput("Timestamp test")

        composeTestRule.onNodeWithText(
            "Post"
        ).performClick()

        val afterCreate = System.currentTimeMillis()

        composeTestRule.runOnIdle {
            val createdAt = savedPost?.createdAt

            check(createdAt != null)
            check(createdAt in beforeCreate..afterCreate)
        }
    }

    @Test
    fun createPost_withWhitespaceAroundCaption_savesTrimmedCaption() {
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
        ).performTextInput("  Hello FriendZone  ")

        composeTestRule.onNodeWithText(
            "Post"
        ).performClick()

        composeTestRule.runOnIdle {
            check(savedPost?.caption == "Hello FriendZone")
        }
    }

    @Test
    fun createPost_withCurrentProfile_clearsCaptionAfterSave() {
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

        composeTestRule.onNodeWithText(
            "What is on your mind?"
        ).performTextInput("Second Post")

        composeTestRule.onNodeWithText(
            "Post"
        ).performClick()

        composeTestRule.runOnIdle {
            check(savedPost?.caption == "Second Post")
        }
    }
}
