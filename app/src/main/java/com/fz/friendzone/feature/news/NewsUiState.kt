package com.fz.friendzone.feature.news

import androidx.annotation.StringRes
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.Profile

sealed interface NewsUiState {

    data object Loading : NewsUiState

    data class Success(
        val posts: List<NewsPostUiModel>
    ) : NewsUiState

    data class Error(
        @StringRes val messageResId: Int
    ) : NewsUiState
}

data class NewsPostUiModel(
    val post: Post,
    val profile: Profile?
)
