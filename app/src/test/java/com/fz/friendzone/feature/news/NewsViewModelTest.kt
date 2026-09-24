package com.fz.friendzone.feature.news

import com.fz.friendzone.R
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.data.repository.NewsRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class NewsViewModelTest {

    @Test
    fun loadAction_updatesUiStateWithPosts() {
        val posts = listOf(
            Post(
                id = "post-1",
                userId = "user-1",
                caption = "Test post"
            )
        )

        val repository = object : NewsRepository {
            override fun getPosts(): List<Post> {
                return posts
            }
        }

        val viewModel = NewsViewModel(
            repository = repository
        )

        viewModel.onAction(NewsAction.Load)

        assertEquals(
            NewsUiState.Success(posts),
            viewModel.uiState.value
        )
    }

    @Test
    fun loadAction_whenRepositoryFails_updatesUiStateWithError() {
        val repository = object : NewsRepository {
            override fun getPosts(): List<Post> {
                return repositoryError()
            }
        }

        val viewModel = NewsViewModel(
            repository = repository
        )

        viewModel.onAction(NewsAction.Load)

        assertEquals(
            NewsUiState.Error(R.string.news_load_error),
            viewModel.uiState.value
        )
    }

    private fun repositoryError(): List<Post> {
        throw IllegalStateException("Load failed")
    }
}
