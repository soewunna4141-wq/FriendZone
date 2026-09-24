package com.fz.friendzone.feature.news

import androidx.annotation.StringRes
import com.fz.friendzone.core.model.Post

sealed interface NewsUiState {

    data object Loading : NewsUiState

    data class Success(
        val posts: List<Post>
    ) : NewsUiState

    data class Error(
        @StringRes val messageResId: Int
    ) : NewsUiState
}
