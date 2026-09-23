package com.fz.friendzone.feature.news

import com.fz.friendzone.core.model.Post

sealed interface NewsUiState {

    data object Loading : NewsUiState

    data class Success(
        val posts: List<Post>
    ) : NewsUiState

    data class Error(
        val message: String
    ) : NewsUiState
}
