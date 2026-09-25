package com.fz.friendzone.feature.news

import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.ProfileRepository
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

            override fun savePost(post: Post) {
            }
        }

        val profileRepository = object : ProfileRepository {
            override fun getProfile() = null

            override fun saveProfile(profile: Profile) {
            }
        }

        val viewModel = NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository
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

            override fun savePost(post: Post) {
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
            profileRepository = profileRepository
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

            override fun savePost(post: Post) {
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
            profileRepository = profileRepository
        )

        viewModel.onAction(NewsAction.Load)

        val state = viewModel.uiState.value as NewsUiState.Success

        assertNull(
            state.posts.single().profile
        )
    }

    @Test
    fun loadPosts_whenRepositoryFails_returnsErrorState() {
        val newsRepository = object : NewsRepository {
            override fun getPosts(): List<Post> {
                error("Test error")
            }

            override fun savePost(post: Post) {
            }
        }

        val profileRepository = object : ProfileRepository {
            override fun getProfile() = null

            override fun saveProfile(profile: Profile) {
            }
        }

        val viewModel = NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository
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

            override fun savePost(post: Post) {
                savedPosts.add(post)
            }
        }

        val profileRepository = object : ProfileRepository {
            override fun getProfile() = null

            override fun saveProfile(profile: Profile) {
            }
        }

        val viewModel = NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository
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
}
