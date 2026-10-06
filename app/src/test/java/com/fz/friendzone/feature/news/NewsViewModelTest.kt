package com.fz.friendzone.feature.news

import com.fz.friendzone.core.media.MediaLibrary
import com.fz.friendzone.core.model.Comment
import com.fz.friendzone.core.model.MediaAsset
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.PostLifecycleState
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

        val newsRepository = FakeNewsRepository(
            activePosts = posts
        )

        val viewModel = createViewModel(
            newsRepository = newsRepository,
            profileRepository = FakeProfileRepository()
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
        val viewModel = createViewModel(
            newsRepository = FakeNewsRepository(),
            profileRepository = FakeProfileRepository()
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

        val viewModel = createViewModel(
            newsRepository = FakeNewsRepository(
                activePosts = listOf(post)
            ),
            profileRepository = FakeProfileRepository(
                currentProfile = profile
            )
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

        val viewModel = createViewModel(
            newsRepository = FakeNewsRepository(
                activePosts = listOf(post)
            ),
            profileRepository = FakeProfileRepository(
                currentProfile = profile
            )
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

        val viewModel = createViewModel(
            newsRepository = FakeNewsRepository(
                activePosts = listOf(post)
            ),
            profileRepository = FakeProfileRepository(),
            reactionRepository = FakeReactionRepository(
                reactions = reactions
            ),
            commentRepository = FakeCommentRepository(
                comments = comments
            )
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
        val newsRepository = FakeNewsRepository(
            loadError = IllegalStateException("Test error")
        )

        val viewModel = createViewModel(
            newsRepository = newsRepository,
            profileRepository = FakeProfileRepository()
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

        val newsRepository = FakeNewsRepository()

        val viewModel = createViewModel(
            newsRepository = newsRepository,
            profileRepository = FakeProfileRepository()
        )

        viewModel.onAction(
            NewsAction.CreatePost(post)
        )

        assertEquals(
            listOf(post),
            newsRepository.getPosts()
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

        val viewModel = createViewModel(
            newsRepository = FakeNewsRepository(
                saveError = IllegalStateException("Test save error")
            ),
            profileRepository = FakeProfileRepository()
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

        val newsRepository = FakeNewsRepository()

        val viewModel = createViewModel(
            newsRepository = newsRepository,
            profileRepository = FakeProfileRepository()
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
    fun updatePost_ownerCannotChangeLifecycleFields() {
        val existingPost = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Original caption",
            createdAt = 1000L,
            lifecycleState = PostLifecycleState.BIN,
            deletedAt = 2000L
        )

        val requestedUpdate = existingPost.copy(
            caption = "Updated caption",
            id = "different-id",
            userId = "different-user",
            createdAt = 9999L,
            lifecycleState = PostLifecycleState.ACTIVE,
            deletedAt = null
        )

        val newsRepository = FakeNewsRepository(
            binPosts = listOf(existingPost)
        )

        val viewModel = createViewModel(
            newsRepository = newsRepository,
            profileRepository = FakeProfileRepository(
                currentProfile = Profile(
                    userId = "user-1",
                    displayName = "Owner"
                )
            )
        )

        viewModel.onAction(
            NewsAction.UpdatePost(requestedUpdate)
        )

        val updatedPost = newsRepository.getBinPosts().single()

        assertEquals(
            "post-1",
            updatedPost.id
        )

        assertEquals(
            "user-1",
            updatedPost.userId
        )

        assertEquals(
            1000L,
            updatedPost.createdAt
        )

        assertEquals(
            PostLifecycleState.BIN,
            updatedPost.lifecycleState
        )

        assertEquals(
            2000L,
            updatedPost.deletedAt
        )

        assertEquals(
            "Updated caption",
            updatedPost.caption
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

        val reactionRepository = FakeReactionRepository()

        val viewModel = createViewModel(
            newsRepository = FakeNewsRepository(
                activePosts = listOf(post)
            ),
            profileRepository = FakeProfileRepository(),
            reactionRepository = reactionRepository
        )

        viewModel.onAction(
            NewsAction.SaveReaction(reaction)
        )

        assertEquals(
            listOf(reaction),
            reactionRepository.getReactions(post.id)
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

        val commentRepository = FakeCommentRepository()

        val viewModel = createViewModel(
            newsRepository = FakeNewsRepository(
                activePosts = listOf(post)
            ),
            profileRepository = FakeProfileRepository(),
            commentRepository = commentRepository
        )

        viewModel.onAction(
            NewsAction.SaveComment(comment)
        )

        assertEquals(
            listOf(comment),
            commentRepository.getComments(post.id)
        )

        val state = viewModel.uiState.value as NewsUiState.Success

        assertEquals(
            listOf(comment),
            state.posts.single().comments
        )
    }

    @Test
    fun movePostToBin_ownerActivePost_movesToBinAndReloads() {
        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Test post"
        )

        val newsRepository = FakeNewsRepository(
            activePosts = listOf(post)
        )

        val viewModel = createViewModel(
            newsRepository = newsRepository,
            profileRepository = FakeProfileRepository(
                currentProfile = Profile(
                    userId = "user-1",
                    displayName = "Owner"
                )
            )
        )

        viewModel.onAction(
            NewsAction.MovePostToBin(post.id)
        )

        assertEquals(
            PostLifecycleState.BIN,
            newsRepository.getBinPosts().single().lifecycleState
        )

        assertEquals(
            NewsUiState.Success(
                posts = emptyList()
            ),
            viewModel.uiState.value
        )
    }

    @Test
    fun movePostToBin_nonOwner_doesNotChangePost() {
        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Test post"
        )

        val newsRepository = FakeNewsRepository(
            activePosts = listOf(post)
        )

        val viewModel = createViewModel(
            newsRepository = newsRepository,
            profileRepository = FakeProfileRepository(
                currentProfile = Profile(
                    userId = "user-2",
                    displayName = "Other User"
                )
            )
        )

        viewModel.onAction(
            NewsAction.MovePostToBin(post.id)
        )

        assertEquals(
            listOf(post),
            newsRepository.getPosts()
        )

        assertEquals(
            emptyList<Post>(),
            newsRepository.getBinPosts()
        )
    }

    @Test
    fun restorePost_ownerBinPost_restoresToActiveAndReloads() {
        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Test post",
            lifecycleState = PostLifecycleState.BIN,
            deletedAt = 2000L
        )

        val newsRepository = FakeNewsRepository(
            binPosts = listOf(post)
        )

        val viewModel = createViewModel(
            newsRepository = newsRepository,
            profileRepository = FakeProfileRepository(
                currentProfile = Profile(
                    userId = "user-1",
                    displayName = "Owner"
                )
            )
        )

        viewModel.onAction(
            NewsAction.RestorePost(post.id)
        )

        assertEquals(
            PostLifecycleState.ACTIVE,
            newsRepository.getPosts().single().lifecycleState
        )

        assertEquals(
            NewsUiState.Success(
                posts = listOf(
                    NewsPostUiModel(
                        post = post.copy(
                            lifecycleState = PostLifecycleState.ACTIVE,
                            deletedAt = null
                        ),
                        profile = null
                    )
                )
            ),
            viewModel.uiState.value
        )
    }

    @Test
    fun restorePost_nonOwner_doesNotChangePost() {
        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Test post",
            lifecycleState = PostLifecycleState.BIN,
            deletedAt = 2000L
        )

        val newsRepository = FakeNewsRepository(
            binPosts = listOf(post)
        )

        val viewModel = createViewModel(
            newsRepository = newsRepository,
            profileRepository = FakeProfileRepository(
                currentProfile = Profile(
                    userId = "user-2",
                    displayName = "Other User"
                )
            )
        )

        viewModel.onAction(
            NewsAction.RestorePost(post.id)
        )

        assertEquals(
            listOf(post),
            newsRepository.getBinPosts()
        )
    }

    @Test
    fun movePostToAsh_ownerActivePost_movesToAshAndReloads() {
        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Test post"
        )

        val newsRepository = FakeNewsRepository(
            activePosts = listOf(post)
        )

        val viewModel = createViewModel(
            newsRepository = newsRepository,
            profileRepository = FakeProfileRepository(
                currentProfile = Profile(
                    userId = "user-1",
                    displayName = "Owner"
                )
            )
        )

        viewModel.onAction(
            NewsAction.MovePostToAsh(post.id)
        )

        assertEquals(
            PostLifecycleState.ASH,
            newsRepository.getAshPosts().single().lifecycleState
        )

        assertEquals(
            NewsUiState.Success(
                posts = emptyList()
            ),
            viewModel.uiState.value
        )
    }

    @Test
    fun movePostToAsh_nonOwner_doesNotChangePost() {
        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Test post"
        )

        val newsRepository = FakeNewsRepository(
            activePosts = listOf(post)
        )

        val viewModel = createViewModel(
            newsRepository = newsRepository,
            profileRepository = FakeProfileRepository(
                currentProfile = Profile(
                    userId = "user-2",
                    displayName = "Other User"
                )
            )
        )

        viewModel.onAction(
            NewsAction.MovePostToAsh(post.id)
        )

        assertEquals(
            listOf(post),
            newsRepository.getPosts()
        )

        assertEquals(
            emptyList<Post>(),
            newsRepository.getAshPosts()
        )
    }

    private fun createViewModel(
        newsRepository: NewsRepository,
        profileRepository: ProfileRepository,
        reactionRepository: ReactionRepository = FakeReactionRepository(),
        commentRepository: CommentRepository = FakeCommentRepository()
    ): NewsViewModel {
        return NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository,
            reactionRepository = reactionRepository,
            commentRepository = commentRepository,
            mediaLibrary = FakeMediaLibrary()
        )
    }

    private class FakeMediaLibrary : MediaLibrary {

        override fun add(asset: MediaAsset) {
        }

        override fun getById(id: String): MediaAsset? {
            return null
        }

        override fun getByOwner(ownerId: String): List<MediaAsset> {
            return emptyList()
        }

        override fun getAll(): List<MediaAsset> {
            return emptyList()
        }

        override fun delete(id: String) {
        }
    }

    private class FakeNewsRepository(
        activePosts: List<Post> = emptyList(),
        binPosts: List<Post> = emptyList(),
        private val loadError: Throwable? = null,
        private val saveError: Throwable? = null
    ) : NewsRepository {

        private val posts = mutableListOf<Post>()

        init {
            posts.addAll(activePosts)
            posts.addAll(binPosts)
        }

        private val ashPosts: List<Post>
            get() = posts.filter { post ->
                post.lifecycleState == PostLifecycleState.ASH
            }

        override fun getPosts(): List<Post> {
            loadError?.let { throw it }

            return posts
                .filter { post ->
                    post.lifecycleState == PostLifecycleState.ACTIVE
                }
                .sortedByDescending { post ->
                    post.createdAt
                }
        }

        override fun getBinPosts(): List<Post> {
            return posts
                .filter { post ->
                    post.lifecycleState == PostLifecycleState.BIN
                }
                .sortedByDescending { post ->
                    post.deletedAt ?: post.createdAt
                }
        }

        override fun savePost(post: Post) {
            saveError?.let { throw it }

            posts.add(
                post.copy(
                    lifecycleState = PostLifecycleState.ACTIVE,
                    deletedAt = null
                )
            )
        }

        override fun updatePost(post: Post) {
            val index = posts.indexOfFirst { existingPost ->
                existingPost.id == post.id
            }

            if (index >= 0) {
                posts[index] = post
            }
        }

        override fun movePostToBin(postId: String) {
            val index = posts.indexOfFirst { post ->
                post.id == postId
            }

            if (index >= 0) {
                posts[index] = posts[index].copy(
                    lifecycleState = PostLifecycleState.BIN,
                    deletedAt = System.currentTimeMillis()
                )
            }
        }

        override fun restorePost(postId: String) {
            val index = posts.indexOfFirst { post ->
                post.id == postId
            }

            if (index >= 0) {
                posts[index] = posts[index].copy(
                    lifecycleState = PostLifecycleState.ACTIVE,
                    deletedAt = null
                )
            }
        }

        override fun movePostToAsh(postId: String) {
            val index = posts.indexOfFirst { post ->
                post.id == postId
            }

            if (index >= 0) {
                posts[index] = posts[index].copy(
                    lifecycleState = PostLifecycleState.ASH,
                    deletedAt = System.currentTimeMillis()
                )
            }
        }

        fun getAshPosts(): List<Post> {
            return ashPosts
        }
    }

    private class FakeProfileRepository(
        private val currentProfile: Profile? = null
    ) : ProfileRepository {

        override fun getProfile(): Profile? {
            return currentProfile
        }

        override fun getProfile(userId: String): Profile? {
            return currentProfile?.takeIf { profile ->
                profile.userId == userId
            }
        }

        override fun saveProfile(profile: Profile) {
        }
    }

    private class FakeReactionRepository(
        private val reactions: MutableList<Reaction> = mutableListOf()
    ) : ReactionRepository {

        constructor(
            reactions: List<Reaction>
        ) : this(
            reactions.toMutableList()
        )

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
        private val comments: MutableList<Comment> = mutableListOf()
    ) : CommentRepository {

        constructor(
            comments: List<Comment>
        ) : this(
            comments.toMutableList()
        )

        override fun getComments(postId: String): List<Comment> {
            return comments.filter { comment ->
                comment.postId == postId
            }
        }

        override fun saveComment(comment: Comment) {
            comments.add(comment)
        }
    }
}
