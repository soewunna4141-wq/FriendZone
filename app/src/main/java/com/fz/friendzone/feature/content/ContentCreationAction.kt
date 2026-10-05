package com.fz.friendzone.feature.content

import com.fz.friendzone.core.model.MediaAsset

sealed interface ContentCreationAction {
    data class MediaSelected(val mediaAsset: MediaAsset) : ContentCreationAction
    data class CaptionChanged(val caption: String) : ContentCreationAction
    data object Create : ContentCreationAction
}
