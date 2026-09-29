package com.fz.friendzone.feature.reels

import androidx.annotation.StringRes
import com.fz.friendzone.core.model.Reel

sealed interface ReelsUiState {

    data object Loading : ReelsUiState

    data class Success(
        val reels: List<Reel>
    ) : ReelsUiState

    data class Error(
        @StringRes val messageResId: Int
    ) : ReelsUiState
}
