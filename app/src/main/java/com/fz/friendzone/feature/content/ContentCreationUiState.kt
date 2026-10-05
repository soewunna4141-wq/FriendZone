package com.fz.friendzone.feature.content

import com.fz.friendzone.core.model.MediaAsset

data class ContentCreationUiState(
    val target: ContentCreationTarget,
    val selectedMedia: MediaAsset? = null,
    val caption: String = "",
    val isLoading: Boolean = false,
    val isCreated: Boolean = false,
    val errorMessage: String? = null
)
