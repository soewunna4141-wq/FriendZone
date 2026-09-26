package com.fz.friendzone.feature.news

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.core.model.Reaction
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.ProfileRepository
import com.fz.friendzone.data.repository.ReactionRepository
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun newsScreen_withNoPosts_showsEmptyState() {
        val profileRepository = FakeProfileRepository()
        val newsRepository = FakeNewsRepository()
        val reactionRepository = FakeReactionRepository()

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository,
                reactionRepository = reactionRepository
            )
        }

        composeTestRule.onNodeWithText("No posts yet").assertIsDisplayed()
    }

    @Test
    fun newsScreen_withPosts_showsPostCaption() {
        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Hello from FriendZone"
        )

        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        val profileRepository = FakeProfileRepository(
            profile = profile
        )
        val newsRepository = FakeNewsRepository(
            posts = listOf(post)
        )
        val reactionRepository = FakeReactionRepository()

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository,
                reactionRepository = reactionRepository
            )
        }

        composeTestRule.onNodeWithText(
            "Hello from FriendZone"
        ).assertIsDisplayed()
    }

    @Test
    fun newsScreen_withPosts_showsAuthorName() {
        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Hello from FriendZone"
        )

        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        val profileRepository = FakeProfileRepository(
            profile = profile
        )
        val newsRepository = FakeNewsRepository(
            posts = listOf(post)
        )
        val reactionRepository = FakeReactionRepository()

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository,
                reactionRepository = reactionRepository
            )
        }

        composeTestRule.onNodeWithText(
            "Test User"
        ).assertIsDisplayed()
    }

    @Test
    fun likePost_withCurrentProfile_savesLikeReaction() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        val post = Post(
            id = "post-1",
            userId = "user-2",
            caption = "Hello from FriendZone"
        )

        val reactionRepository = FakeReactionRepository()

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
                profileRepository = profileRepository,
                reactionRepository = reactionRepository
            )
        }

        composeTestRule.onNodeWithText(
            "Like"
        ).performClick()

        composeTestRule.runOnIdle {
            val reactions = reactionRepository.getReactions("post-1")

            check(reactions.size == 1)

            val savedReaction = reactions.single()

            check(savedReaction.postId == "post-1")
            check(savedReaction.userId == "user-1")
            check(savedReaction.type == "LIKE")
        }
    }

    @Test
    fun likePost_updatesReactionCount() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        val post = Post(
            id = "post-1",
            userId = "user-2",
            caption = "Hello from FriendZone"
        )

        val reactionRepository = FakeReactionRepository()

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
                profileRepository = profileRepository,
                reactionRepository = reactionRepository
            )
        }

        composeTestRule.onNodeWithText("0").assertIsDisplayed()

        composeTestRule.onNodeWithText(
            "Like"
        ).performClick()

        composeTestRule.onNodeWithText("1").assertIsDisplayed()
    }

    private class FakeNewsRepository(
        private val posts: List<Post> = emptyList()
    ) : NewsRepository {

        override fun getPosts(): List<Post> {
            return posts
        }

        override fun savePost(post: Post) {
        }
    }

    private class FakeProfileRepository(
        private val profile: Profile? = null
    ) : ProfileRepository {

        override fun getProfile(): Profile? {
            return profile
        }

        override fun getProfile(userId: String): Profile? {
            return profile?.takeIf { it.userId == userId }
        }

        override fun saveProfile(profile: Profile) {
        }
    }

    private class FakeReactionRepository : ReactionRepository {

        private val reactions = mutableListOf<Reaction>()

        override fun getReactions(postId: String): List<Reaction> {
            return reactions.filter { reaction ->
                reaction.postId == postId
            }
        }

        override fun saveReaction(reaction: Reaction) {
            reactions.add(reaction)
        }
    }
}
