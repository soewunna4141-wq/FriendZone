package com.fz.friendzone.feature.news

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fz.friendzone.core.media.MediaLibrary
import com.fz.friendzone.core.model.Comment
import com.fz.friendzone.core.model.Follow
import com.fz.friendzone.core.model.Friend
import com.fz.friendzone.core.model.MediaAsset
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.PostAudience
import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.core.model.Reaction
import com.fz.friendzone.data.repository.CommentRepository
import com.fz.friendzone.data.repository.FollowRepository
import com.fz.friendzone.data.repository.FriendRepository
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

    private val friendRepository = FakeFriendRepository()
    private val followRepository = FakeFollowRepository()

    @Test
    fun newsScreen_withNoPosts_showsEmptyState() {
        val profileRepository = FakeProfileRepository()
        val newsRepository = FakeNewsRepository()
        val reactionRepository = FakeReactionRepository()
        val commentRepository = FakeCommentRepository()
        val mediaLibrary = FakeMediaLibrary()

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository,
                reactionRepository = reactionRepository,
                commentRepository = commentRepository,
                mediaLibrary = mediaLibrary,
                friendRepository = friendRepository,
                followRepository = followRepository
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
        val commentRepository = FakeCommentRepository()
        val mediaLibrary = FakeMediaLibrary()

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository,
                reactionRepository = reactionRepository,
                commentRepository = commentRepository,
                mediaLibrary = mediaLibrary,
                friendRepository = friendRepository,
                followRepository = followRepository
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
        val commentRepository = FakeCommentRepository()
        val mediaLibrary = FakeMediaLibrary()

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository,
                reactionRepository = reactionRepository,
                commentRepository = commentRepository,
                mediaLibrary = mediaLibrary,
                friendRepository = friendRepository,
                followRepository = followRepository
            )
        }

        composeTestRule.onNodeWithText(
            "Test User"
        ).assertIsDisplayed()
    }

    @Test
    fun createPost_withCurrentProfile_savesPostAndShowsPost() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        val savedPosts = mutableListOf<Post>()

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
                return savedPosts.toList()
            }

            override fun getBinPosts(): List<Post> {
                return emptyList()
            }

            override fun savePost(post: Post) {
                savedPosts.add(post)
            }

            override fun updatePost(post: Post) {
            }

            override fun movePostToBin(postId: String) {
            }

            override fun restorePost(postId: String) {
            }

            override fun movePostToAsh(postId: String) {
            }
        }

        val reactionRepository = FakeReactionRepository()
        val commentRepository = FakeCommentRepository()
        val mediaLibrary = FakeMediaLibrary()

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository,
                reactionRepository = reactionRepository,
                commentRepository = commentRepository,
                mediaLibrary = mediaLibrary,
                friendRepository = friendRepository,
                followRepository = followRepository
            )
        }

        composeTestRule
            .onAllNodes(hasSetTextAction())[0]
            .performTextInput("Hello from FriendZone")

        composeTestRule.onNodeWithText(
            "Post"
        ).performClick()

        composeTestRule.onNodeWithText(
            "Hello from FriendZone"
        ).assertIsDisplayed()

        composeTestRule.runOnIdle {
            check(savedPosts.size == 1)

            val savedPost = savedPosts.single()

            check(savedPost.userId == "user-1")
            check(savedPost.caption == "Hello from FriendZone")
        }
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
        val commentRepository = FakeCommentRepository()
        val mediaLibrary = FakeMediaLibrary()

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

            override fun getBinPosts(): List<Post> {
                return emptyList()
            }

            override fun savePost(post: Post) {
            }

            override fun updatePost(post: Post) {
            }

            override fun movePostToBin(postId: String) {
            }

            override fun restorePost(postId: String) {
            }

            override fun movePostToAsh(postId: String) {
            }
        }

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository,
                reactionRepository = reactionRepository,
                commentRepository = commentRepository,
                mediaLibrary = mediaLibrary,
                friendRepository = friendRepository,
                followRepository = followRepository
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
        val commentRepository = FakeCommentRepository()
        val mediaLibrary = FakeMediaLibrary()

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

            override fun getBinPosts(): List<Post> {
                return emptyList()
            }

            override fun savePost(post: Post) {
            }

            override fun updatePost(post: Post) {
            }

            override fun movePostToBin(postId: String) {
            }

            override fun restorePost(postId: String) {
            }

            override fun movePostToAsh(postId: String) {
            }
        }

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository,
                reactionRepository = reactionRepository,
                commentRepository = commentRepository,
                mediaLibrary = mediaLibrary,
                friendRepository = friendRepository,
                followRepository = followRepository
            )
        }

        composeTestRule.onNodeWithText("0").assertIsDisplayed()

        composeTestRule.onNodeWithText(
            "Like"
        ).performClick()

        composeTestRule.onNodeWithText("1").assertIsDisplayed()
    }

    @Test
    fun commentPost_withCurrentProfile_savesComment() {
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
        val commentRepository = FakeCommentRepository()
        val mediaLibrary = FakeMediaLibrary()

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

            override fun getBinPosts(): List<Post> {
                return emptyList()
            }

            override fun savePost(post: Post) {
            }

            override fun updatePost(post: Post) {
            }

            override fun movePostToBin(postId: String) {
            }

            override fun restorePost(postId: String) {
            }

            override fun movePostToAsh(postId: String) {
            }
        }

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository,
                reactionRepository = reactionRepository,
                commentRepository = commentRepository,
                mediaLibrary = mediaLibrary,
                friendRepository = friendRepository,
                followRepository = followRepository
            )
        }

        composeTestRule
            .onAllNodes(hasSetTextAction())[1]
            .performTextInput("Nice post")

        composeTestRule.onNodeWithText(
            "Comment"
        ).performClick()

        composeTestRule.onNodeWithText(
            "Nice post"
        ).assertIsDisplayed()

        composeTestRule.runOnIdle {
            val comments = commentRepository.getComments("post-1")

            check(comments.size == 1)

            val savedComment = comments.single()

            check(savedComment.postId == "post-1")
            check(savedComment.userId == "user-1")
            check(savedComment.text == "Nice post")
        }
    }

    @Test
    fun newsScreen_withComments_showsCommentText() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        val post = Post(
            id = "post-1",
            userId = "user-2",
            caption = "Hello from FriendZone"
        )

        val comment = Comment(
            id = "comment-1",
            postId = "post-1",
            userId = "user-1",
            text = "Nice post"
        )

        val reactionRepository = FakeReactionRepository()
        val commentRepository = FakeCommentRepository(
            comments = listOf(comment)
        )
        val mediaLibrary = FakeMediaLibrary()

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

            override fun getBinPosts(): List<Post> {
                return emptyList()
            }

            override fun savePost(post: Post) {
            }

            override fun updatePost(post: Post) {
            }

            override fun movePostToBin(postId: String) {
            }

            override fun restorePost(postId: String) {
            }

            override fun movePostToAsh(postId: String) {
            }
        }

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository,
                reactionRepository = reactionRepository,
                commentRepository = commentRepository,
                mediaLibrary = mediaLibrary,
                friendRepository = friendRepository,
                followRepository = followRepository
            )
        }

        composeTestRule.onNodeWithText(
            "Nice post"
        ).assertIsDisplayed()
    }

    @Test
    fun privatePost_forFriend_showsPostButBlocksInteractions() {
        val ownerProfile = Profile(
            userId = "user-2",
            displayName = "Owner"
        )

        val viewerProfile = Profile(
            userId = "user-1",
            displayName = "Friend Viewer"
        )

        val post = Post(
            id = "private-post-1",
            userId = "user-2",
            caption = "Private friend post",
            audience = PostAudience.PRIVATE
        )

        val profileRepository = object : ProfileRepository {
            override fun getProfile(): Profile? {
                return viewerProfile
            }

            override fun getProfile(userId: String): Profile? {
                return when (userId) {
                    "user-1" -> viewerProfile
                    "user-2" -> ownerProfile
                    else -> null
                }
            }

            override fun saveProfile(profile: Profile) {
            }
        }

        val newsRepository = FakeNewsRepository(
            posts = listOf(post)
        )
        val reactionRepository = FakeReactionRepository()
        val commentRepository = FakeCommentRepository()
        val mediaLibrary = FakeMediaLibrary()
        val privacyFriendRepository = FakeFriendRepository()

        privacyFriendRepository.saveFriend(
            Friend(
                id = "friend-1",
                userId = "user-1",
                friendUserId = "user-2"
            )
        )

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository,
                reactionRepository = reactionRepository,
                commentRepository = commentRepository,
                mediaLibrary = mediaLibrary,
                friendRepository = privacyFriendRepository,
                followRepository = followRepository
            )
        }

        composeTestRule.onNodeWithText(
            "Private friend post"
        ).assertIsDisplayed()

        composeTestRule.runOnIdle {
            check(
                composeTestRule
                    .onAllNodesWithText("Like")
                    .fetchSemanticsNodes()
                    .isEmpty()
            )

            check(
                composeTestRule
                    .onAllNodesWithText("Write a comment")
                    .fetchSemanticsNodes()
                    .isEmpty()
            )

            check(
                composeTestRule
                    .onAllNodesWithText("Comment")
                    .fetchSemanticsNodes()
                    .isEmpty()
            )
        }
    }

    @Test
    fun ownerPostMenu_showsOwnerActions() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Owner"
        )

        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Owner post"
        )

        val profileRepository = FakeProfileRepository(
            profile = profile
        )
        val newsRepository = FakeNewsRepository(
            posts = listOf(post)
        )
        val reactionRepository = FakeReactionRepository()
        val commentRepository = FakeCommentRepository()
        val mediaLibrary = FakeMediaLibrary()

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository,
                reactionRepository = reactionRepository,
                commentRepository = commentRepository,
                mediaLibrary = mediaLibrary,
                friendRepository = friendRepository,
                followRepository = followRepository
            )
        }

        composeTestRule.onNodeWithText("⋮").performClick()

        composeTestRule.onNodeWithText("Audience").assertIsDisplayed()
        composeTestRule.onNodeWithText("Edit").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete").assertIsDisplayed()
    }

    @Test
    fun viewerPostMenu_showsOnlySave() {
        val ownerProfile = Profile(
            userId = "user-2",
            displayName = "Owner"
        )

        val viewerProfile = Profile(
            userId = "user-1",
            displayName = "Viewer"
        )

        val post = Post(
            id = "post-1",
            userId = "user-2",
            caption = "Viewer post"
        )

        val profileRepository = object : ProfileRepository {
            override fun getProfile(): Profile? {
                return viewerProfile
            }

            override fun getProfile(userId: String): Profile? {
                return when (userId) {
                    "user-1" -> viewerProfile
                    "user-2" -> ownerProfile
                    else -> null
                }
            }

            override fun saveProfile(profile: Profile) {
            }
        }

        val newsRepository = FakeNewsRepository(
            posts = listOf(post)
        )
        val reactionRepository = FakeReactionRepository()
        val commentRepository = FakeCommentRepository()
        val mediaLibrary = FakeMediaLibrary()

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository,
                reactionRepository = reactionRepository,
                commentRepository = commentRepository,
                mediaLibrary = mediaLibrary,
                friendRepository = friendRepository,
                followRepository = followRepository
            )
        }

        composeTestRule.onNodeWithText("⋮").performClick()

        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
    }

    @Test
    fun ownerDeleteMenu_showsBinAndAshActions() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Owner"
        )

        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Delete me"
        )

        val profileRepository = FakeProfileRepository(
            profile = profile
        )
        val newsRepository = FakeNewsRepository(
            posts = listOf(post)
        )
        val reactionRepository = FakeReactionRepository()
        val commentRepository = FakeCommentRepository()
        val mediaLibrary = FakeMediaLibrary()

        composeTestRule.setContent {
            NewsScreen(
                repository = newsRepository,
                profileRepository = profileRepository,
                reactionRepository = reactionRepository,
                commentRepository = commentRepository,
                mediaLibrary = mediaLibrary,
                friendRepository = friendRepository,
                followRepository = followRepository
            )
        }

        composeTestRule.onNodeWithText("⋮").performClick()
        composeTestRule.onNodeWithText("Delete").performClick()

        composeTestRule.onNodeWithText("Delete post").assertIsDisplayed()
        composeTestRule.onNodeWithText(
            "Choose what you want to do with this post."
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText("Move to Bin").assertIsDisplayed()
        composeTestRule.onNodeWithText("Move to Ash").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
    }

    private class FakeNewsRepository(
        private val posts: List<Post> = emptyList()
    ) : NewsRepository {

        override fun getPosts(): List<Post> {
            return posts
        }

        override fun getBinPosts(): List<Post> {
            return emptyList()
        }

        override fun savePost(post: Post) {
        }

        override fun updatePost(post: Post) {
        }

        override fun movePostToBin(postId: String) {
        }

        override fun restorePost(postId: String) {
        }

        override fun movePostToAsh(postId: String) {
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

    private class FakeFriendRepository : FriendRepository {

        private val friends = mutableListOf<Friend>()

        override fun getFriends(userId: String): List<Friend> {
            return friends.filter { friend ->
                friend.userId == userId
            }
        }

        override fun saveFriend(friend: Friend) {
            friends.add(friend)
        }
    }

    private class FakeFollowRepository : FollowRepository {

        private val follows = mutableListOf<Follow>()

        override fun getFollowing(followerId: String): List<Follow> {
            return follows.filter { follow ->
                follow.followerId == followerId
            }
        }

        override fun getFollowers(followingId: String): List<Follow> {
            return follows.filter { follow ->
                follow.followingId == followingId
            }
        }

        override fun saveFollow(follow: Follow) {
            follows.add(follow)
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

    private class FakeCommentRepository(
        private val comments: List<Comment> = emptyList()
    ) : CommentRepository {

        private val savedComments = comments.toMutableList()

        override fun getComments(postId: String): List<Comment> {
            return savedComments.filter { comment ->
                comment.postId == postId
            }
        }

        override fun saveComment(comment: Comment) {
            savedComments.add(comment)
        }
    }

    private class FakeMediaLibrary : MediaLibrary {

        private val assets = mutableListOf<MediaAsset>()

        override fun add(asset: MediaAsset) {
            assets.removeAll { it.id == asset.id }
            assets.add(asset)
        }

        override fun getById(id: String): MediaAsset? {
            return assets.firstOrNull { it.id == id }
        }

        override fun getByOwner(ownerId: String): List<MediaAsset> {
            return assets.filter { it.ownerId == ownerId }
        }

        override fun getAll(): List<MediaAsset> {
            return assets.toList()
        }

        override fun delete(id: String) {
            assets.removeAll { it.id == id }
        }
    }
}
