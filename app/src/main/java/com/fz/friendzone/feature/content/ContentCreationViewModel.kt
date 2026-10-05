package com.fz.friendzone.feature.content

import androidx.lifecycle.ViewModel
import com.fz.friendzone.core.model.MediaType
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.PostMediaType
import com.fz.friendzone.core.model.Reel
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.ReelsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ContentCreationViewModel(
    private val newsRepository: NewsRepository,
    private val reelsRepository: ReelsRepository,
    target: ContentCreationTarget
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ContentCreationUiState(target = target)
    )

    val uiState: StateFlow<ContentCreationUiState> = _uiState.asStateFlow()

    fun onAction(action: ContentCreationAction) {
        when (action) {
            is ContentCreationAction.MediaSelected -> {
                _uiState.value = _uiState.value.copy(
                    selectedMedia = action.mediaAsset,
                    errorMessage = null
                )
            }

            is ContentCreationAction.CaptionChanged -> {
                _uiState.value = _uiState.value.copy(
                    caption = action.caption,
                    errorMessage = null
                )
            }

            ContentCreationAction.Create -> createContent()
        }
    }

    private fun createContent() {
        val state = _uiState.value
        val media = state.selectedMedia

        if (media == null) {
            _uiState.value = state.copy(
                errorMessage = "Please select media first."
            )
            return
        }

        if (
            state.target == ContentCreationTarget.REELS &&
            media.type != MediaType.VIDEO
        ) {
            _uiState.value = state.copy(
                errorMessage = "Reels require video media."
            )
            return
        }

        _uiState.value = state.copy(
            isLoading = true,
            errorMessage = null
        )

        runCatching {
            when (state.target) {
                ContentCreationTarget.NEWS -> {
                    val postMediaType = when (media.type) {
                        MediaType.IMAGE -> PostMediaType.IMAGE
                        MediaType.VIDEO -> PostMediaType.VIDEO
                    }

                    newsRepository.savePost(
                        Post(
                            id = UUID.randomUUID().toString(),
                            userId = media.ownerId,
                            caption = state.caption,
                            mediaAssetId = media.id,
                            mediaType = postMediaType
                        )
                    )
                }

                ContentCreationTarget.REELS -> {
                    reelsRepository.saveReel(
                        Reel(
                            id = UUID.randomUUID().toString(),
                            userId = media.ownerId,
                            videoUrl = media.uri,
                            caption = state.caption
                        )
                    )
                }
            }
        }.onSuccess {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isCreated = true
            )
        }.onFailure { error ->
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = error.message ?: "Failed to create content."
            )
        }
    }
}
