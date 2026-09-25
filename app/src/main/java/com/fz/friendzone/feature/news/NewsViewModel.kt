package com.fz.friendzone.feature.news

import androidx.lifecycle.ViewModel
import com.fz.friendzone.R
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewsViewModel(
    private val newsRepository: NewsRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)

    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    fun onAction(action: NewsAction) {
        when (action) {
            NewsAction.Load -> loadPosts()
        }
    }

    private fun loadPosts() {
        _uiState.value = NewsUiState.Loading

        runCatching {
            newsRepository.getPosts()
        }.onSuccess { posts ->
            val postUiModels = posts.map { post ->
                NewsPostUiModel(
                    post = post,
                    profile = profileRepository.getProfile(post.userId)
                )
            }

            _uiState.value = NewsUiState.Success(postUiModels)
        }.onFailure {
            _uiState.value = NewsUiState.Error(
                messageResId = R.string.news_load_error
            )
        }
    }
}
