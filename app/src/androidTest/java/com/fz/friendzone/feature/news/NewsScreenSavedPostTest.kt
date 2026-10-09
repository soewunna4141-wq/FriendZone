
package com.fz.friendzone.feature.news

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fz.friendzone.core.media.MediaLibrary
import com.fz.friendzone.core.model.Comment
import com.fz.friendzone.core.model.Follow
import com.fz.friendzone.core.model.Friend
import com.fz.friendzone.core.model.MediaAsset
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.core.model.Reaction
import com.fz.friendzone.core.model.SavedPost
import com.fz.friendzone.core.saved.SavedPostRepository
import com.fz.friendzone.data.repository.CommentRepository
import com.fz.friendzone.data.repository.FollowRepository
import com.fz.friendzone.data.repository.FriendRepository
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.ProfileRepository
import com.fz.friendzone.data.repository.ReactionRepository
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsScreenSavedPostTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun savePost_persistsAndShowsSavedFeedback() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Save this post"
        )

        val savedRepository = FakeSavedPostRepository()

        composeTestRule.setContent {
            NewsScreen(
                repository = FakeNewsRepository(post),
                profileRepository = FakeProfileRepository(profile),
                reactionRepository = FakeReactionRepository(),
                commentRepository = FakeCommentRepository(),
                mediaLibrary = FakeMediaLibrary(),
                friendRepository = FakeFriendRepository(),
                followRepository = FakeFollowRepository(),
                savedPostRepository = savedRepository
            )
        }

        composeTestRule.onNodeWithText("Save")
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithText(
            "Saved to your saved posts."
        ).assertIsDisplayed()

        composeTestRule.onNodeWithText("Done")
            .performClick()

        composeTestRule.onNodeWithText("Saved")
            .assertIsDisplayed()

        composeTestRule.runOnIdle {
            assertTrue(
                savedRepository.isSaved("user-1", "post-1")
            )

            assertTrue(
                savedRepository.getSavedPosts("user-1")
                    .any { it.postId == "post-1" }
            )
        }
    }

    private class FakeSavedPostRepository : SavedPostRepository {

        private val savedPosts =
            linkedMapOf<Pair<String, String>, SavedPost>()

        override fun save(
            userId: String,
            postId: String
        ): Boolean {
            if (userId.isBlank() || postId.isBlank()) {
                return false
            }

            val key = userId to postId

            if (savedPosts.containsKey(key)) {
                return false
            }

            savedPosts[key] = SavedPost(
                userId = userId,
                postId = postId
            )

            return true
        }

        override fun getSavedPosts(
            userId: String
        ): List<SavedPost> {
            if (userId.isBlank()) {
                return emptyList()
            }

            return savedPosts.values.filter {
                it.userId == userId
            }
        }

        override fun isSaved(
            userId: String,
            postId: String
        ): Boolean {
            return savedPosts.containsKey(userId to postId)
        }

        override fun remove(
            userId: String,
            postId: String
        ): Boolean {
            return savedPosts.remove(userId to postId) != null
        }
    }

    private class FakeNewsRepository(
        private val post: Post
    ) : NewsRepository {

        override fun getPosts(): List<Post> = listOf(post)

        override fun getBinPosts(): List<Post> = emptyList()

        override fun savePost(post: Post) {}

        override fun updatePost(post: Post) {}

        override fun movePostToBin(postId: String) {}

        override fun restorePost(postId: String) {}

        override fun movePostToAsh(postId: String) {}
    }

    private class FakeProfileRepository(
        private val profile: Profile
    ) : ProfileRepository {

        override fun getProfile(): Profile = profile

        override fun getProfile(userId: String): Profile? {
            return profile.takeIf { it.userId == userId }
        }

        override fun saveProfile(profile: Profile) {}
    }

    private class FakeReactionRepository : ReactionRepository {

        override fun getReactions(postId: String): List<Reaction> {
            return emptyList()
        }

        override fun saveReaction(reaction: Reaction) {}
    }

    private class FakeCommentRepository : CommentRepository {

        override fun getComments(postId: String): List<Comment> {
            return emptyList()
        }

        override fun saveComment(comment: Comment) {}
    }

    private class FakeFriendRepository : FriendRepository {

        override fun getFriends(userId: String): List<Friend> {
            return emptyList()
        }

        override fun saveFriend(friend: Friend) {}
    }

    private class FakeFollowRepository : FollowRepository {

        override fun getFollowing(followerId: String): List<Follow> {
            return emptyList()
        }

        override fun getFollowers(followingId: String): List<Follow> {
            return emptyList()
        }

        override fun saveFollow(follow: Follow) {}
    }

    private class FakeMediaLibrary : MediaLibrary {

        override fun add(asset: MediaAsset) {}

        override fun getById(id: String): MediaAsset? = null

        override fun getByOwner(ownerId: String): List<MediaAsset> {
            return emptyList()
        }

        override fun getAll(): List<MediaAsset> = emptyList()

        override fun delete(id: String) {}
    }
}
