package com.fz.friendzone.core.media

import android.net.Uri

sealed interface CameraCaptureUiState {

    data object Idle : CameraCaptureUiState

    data object RequestingPermission : CameraCaptureUiState

    data object Ready : CameraCaptureUiState

    data object Recording : CameraCaptureUiState

    data class Captured(
        val uri: Uri,
        val isVideo: Boolean
    ) : CameraCaptureUiState

    data class Error(
        val message: String
    ) : CameraCaptureUiState
}
