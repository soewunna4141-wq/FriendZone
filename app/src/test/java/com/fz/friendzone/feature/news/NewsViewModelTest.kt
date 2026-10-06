package com.fz.friendzone.feature.news

import com.fz.friendzone.core.model.Comment
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.core.model.Reaction
import com.fz.friendzone.data.repository.CommentRepository
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.ProfileRepository
import com.fz.friendzone.data.repository.ReactionRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NewsViewModelTest {

    @Test
    fun loadPosts_returnsSuccessStateWithPosts() {
        val posts = listOf(
            Post(
                id = "post-1",
                userId = "user-1",
                caption = "Test post"
            )
        )

        val newsRepository = object : NewsRepository {
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

        val profileRepository = object : ProfileRepository {
            override fun getProfile() = null

            override fun saveProfile(profile: Profile) {
            }
        }

        val viewModel = NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository,
            reactionRepository = FakeReactionRepository(),
            commentRepository = FakeCommentRepository()
        )

        viewModel.onAction(NewsAction.Load)

        assertEquals(
            NewsUiState.Success(
                posts = listOf(
                    NewsPostUiModel(
                        post = posts[0],
                        profile = null
                    )
                )
            ),
            viewModel.uiState.value
        )
    }

    @Test
    fun loadPosts_whenRepositoryReturnsEmptyList_returnsEmptySuccessState() {
        val newsRepository = object : NewsRepository {
            override fun getPosts(): List<Post> {
                return emptyList()
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

        val profileRepository = object : ProfileRepository {
            override fun getProfile() = null

            override fun saveProfile(profile: Profile) {
            }
        }

        val viewModel = NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository,
            reactionRepository = FakeReactionRepository(),
            commentRepository = FakeCommentRepository()
        )

        viewModel.onAction(NewsAction.Load)

        assertEquals(
            NewsUiState.Success(
                posts = emptyList()
            ),
            viewModel.uiState.value
        )
    }

    @Test
    fun loadPosts_mapsPostUserIdToProfile() {
        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Test post"
        )

        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

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

        val profileRepository = object : ProfileRepository {
            override fun getProfile() = profile

            override fun getProfile(userId: String): Profile? {
                return profile.takeIf { it.userId == userId }
            }

            override fun saveProfile(profile: Profile) {
            }
        }

        val viewModel = NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository,
            reactionRepository = FakeReactionRepository(),
            commentRepository = FakeCommentRepository()
        )

        viewModel.onAction(NewsAction.Load)

        val state = viewModel.uiState.value as NewsUiState.Success

        assertEquals(
            profile,
            state.posts.single().profile
        )
    }

    @Test
    fun loadPosts_whenProfileUserIdDoesNotMatch_returnsNullProfile() {
        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Test post"
        )

        val profile = Profile(
            userId = "user-2",
            displayName = "Other User"
        )

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

        val profileRepository = object : ProfileRepository {
            override fun getProfile() = profile

            override fun getProfile(userId: String): Profile? {
                return profile.takeIf { it.userId == userId }
            }

            override fun saveProfile(profile: Profile) {
            }
        }

        val viewModel = NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository,
            reactionRepository = FakeReactionRepository(),
            commentRepository = FakeCommentRepository()
        )

        viewModel.onAction(NewsAction.Load)

        val state = viewModel.uiState.value as NewsUiState.Success

        assertNull(
            state.posts.single().profile
        )
    }

    @Test
    fun loadPosts_mapsReactionCountAndComments() {
        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Test post"
        )

        val reactions = listOf(
            Reaction(
                id = "reaction-1",
                postId = "post-1",
                userId = "user-1"
            ),
            Reaction(
                id = "reaction-2",
                postId = "post-1",
                userId = "user-2"
            )
        )

        val comments = listOf(
            Comment(
                id = "comment-1",
                postId = "post-1",
                userId = "user-2",
                text = "Nice post"
            ),
            Comment(
                id = "comment-2",
                postId = "post-1",
                userId = "user-3",
                text = "Great post"
            )
        )

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

        val profileRepository = object : ProfileRepository {
            override fun getProfile() = null

            override fun saveProfile(profile: Profile) {
            }
        }

        val reactionRepository = object : ReactionRepository {
            override fun getReactions(postId: String): List<Reaction> {
                return reactions.filter { reaction ->
                    reaction.postId == postId
                }
            }

            override fun saveReaction(reaction: Reaction) {
            }
        }

        val commentRepository = object : CommentRepository {
            override fun getComments(postId: String): List<Comment> {
                return comments.filter { comment ->
                    comment.postId == postId
                }
            }

            override fun saveComment(comment: Comment) {
            }
        }

        val viewModel = NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository,
            reactionRepository = reactionRepository,
            commentRepository = commentRepository
        )

        viewModel.onAction(NewsAction.Load)

        val state = viewModel.uiState.value as NewsUiState.Success
        val postUiModel = state.posts.single()

        assertEquals(
            2,
            postUiModel.reactionCount
        )

        assertEquals(
            comments,
            postUiModel.comments
        )
    }

    @Test
    fun loadPosts_whenRepositoryFails_returnsErrorState() {
        val newsRepository = object : NewsRepository {
            override fun getPosts(): List<Post> {
                error("Test error")
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

        val profileRepository = object : ProfileRepository {
            override fun getProfile() = null

            override fun saveProfile(profile: Profile) {
            }
        }

        val viewModel = NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository,
            reactionRepository = FakeReactionRepository(),
            commentRepository = FakeCommentRepository()
        )

        viewModel.onAction(NewsAction.Load)

        assertEquals(
            NewsUiState.Error(
                messageResId = com.fz.friendzone.R.string.news_load_error
            ),
            viewModel.uiState.value
        )
    }

    @Test
    fun createPost_savesPostAndReloadsPosts() {
        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Created post"
        )

        val savedPosts = mutableListOf<Post>()

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

        val profileRepository = object : ProfileRepository {
            override fun getProfile() = null

            override fun saveProfile(profile: Profile) {
            }
        }

        val viewModel = NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository,
            reactionRepository = FakeReactionRepository(),
            commentRepository = FakeCommentRepository()
        )

        viewModel.onAction(
            NewsAction.CreatePost(post)
        )

        assertEquals(
            listOf(post),
            savedPosts
        )

        assertEquals(
            NewsUiState.Success(
                posts = listOf(
                    NewsPostUiModel(
                        post = post,
                        profile = null
                    )
                )
            ),
            viewModel.uiState.value
        )
    }

    @Test
    fun createPost_whenSaveFails_returnsErrorState() {
        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Failed post"
        )

        val newsRepository = object : NewsRepository {
            override fun getPosts(): List<Post> {
                return emptyList()
            }

            override fun getBinPosts(): List<Post> {
                return emptyList()
            }

            override fun savePost(post: Post) {
                error("Test save error")
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

        val profileRepository = object : ProfileRepository {
            override fun getProfile() = null

            override fun saveProfile(profile: Profile) {
            }
        }

        val viewModel = NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository,
            reactionRepository = FakeReactionRepository(),
            commentRepository = FakeCommentRepository()
        )

        viewModel.onAction(
            NewsAction.CreatePost(post)
        )

        assertEquals(
            NewsUiState.Error(
                messageResId = com.fz.friendzone.R.string.news_load_error
            ),
            viewModel.uiState.value
        )
    }

    @Test
    fun createPost_reloadsPostsInRepositoryOrder() {
        val olderPost = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Older post",
            createdAt = 1000L
        )

        val newerPost = Post(
            id = "post-2",
            userId = "user-2",
            caption = "Newer post",
            createdAt = 2000L
        )

        val savedPosts = mutableListOf<Post>()

        val newsRepository = object : NewsRepository {
            override fun getPosts(): List<Post> {
                return savedPosts.sortedByDescending { post ->
                    post.createdAt
                }
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

        val profileRepository = object : ProfileRepository {
            override fun getProfile() = null

            override fun getProfile(userId: String): Profile? {
                return null
            }

            override fun saveProfile(profile: Profile) {
            }
        }

        val viewModel = NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository,
            reactionRepository = FakeReactionRepository(),
            commentRepository = FakeCommentRepository()
        )

        viewModel.onAction(
            NewsAction.CreatePost(olderPost)
        )

        viewModel.onAction(
            NewsAction.CreatePost(newerPost)
        )

        val state = viewModel.uiState.value as NewsUiState.Success

        assertEquals(
            listOf(
                NewsPostUiModel(
                    post = newerPost,
                    profile = null
                ),
                NewsPostUiModel(
                    post = olderPost,
                    profile = null
                )
            ),
            state.posts
        )
    }

    @Test
    fun saveReaction_savesReactionAndReloadsPosts() {
        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Test post"
        )

        val reaction = Reaction(
            id = "reaction-1",
            postId = "post-1",
            userId = "user-2"
        )

        val savedReactions = mutableListOf<Reaction>()

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

        val profileRepository = object : ProfileRepository {
            override fun getProfile() = null

            override fun saveProfile(profile: Profile) {
            }
        }

        val reactionRepository = object : ReactionRepository {
            override fun getReactions(postId: String): List<Reaction> {
                return savedReactions.filter { it.postId == postId }
            }

            override fun saveReaction(reaction: Reaction) {
                savedReactions.add(reaction)
            }
        }

        val viewModel = NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository,
            reactionRepository = reactionRepository,
            commentRepository = FakeCommentRepository()
        )

        viewModel.onAction(
            NewsAction.SaveReaction(reaction)
        )

        assertEquals(
            listOf(reaction),
            savedReactions
        )

        val state = viewModel.uiState.value as NewsUiState.Success

        assertEquals(
            1,
            state.posts.single().reactionCount
        )
    }

    @Test
    fun saveComment_savesCommentAndReloadsPosts() {
        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Test post"
        )

        val comment = Comment(
            id = "comment-1",
            postId = "post-1",
            userId = "user-2",
            text = "Nice post"
        )

        val savedComments = mutableListOf<Comment>()

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

        val profileRepository = object : ProfileRepository {
            override fun getProfile() = null

            override fun saveProfile(profile: Profile) {
            }
        }

        val commentRepository = object : CommentRepository {
            override fun getComments(postId: String): List<Comment> {
                return savedComments.filter { it.postId == postId }
            }

            override fun saveComment(comment: Comment) {
                savedComments.add(comment)
            }
        }

        val viewModel = NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository,
            reactionRepository = FakeReactionRepository(),
            commentRepository = commentRepository
        )

        viewModel.onAction(
            NewsAction.SaveComment(comment)
        )

        assertEquals(
            listOf(comment),
            savedComments
        )

        val state = viewModel.uiState.value as NewsUiState.Success

        assertEquals(
            listOf(comment),
            state.posts.single().comments
        )
    }

    private class FakeReactionRepository : ReactionRepository {

        override fun getReactions(postId: String): List<Reaction> {
            return emptyList()
        }

        override fun saveReaction(reaction: Reaction) {
        }
    }

    private class FakeCommentRepository : CommentRepository {

        override fun getComments(postId: String): List<Comment> {
            return emptyList()
        }

        override fun saveComment(comment: Comment) {
        }
    }
}
