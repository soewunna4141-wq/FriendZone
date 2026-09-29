package com.fz.friendzone.feature.reels

import androidx.lifecycle.ViewModel
import com.fz.friendzone.data.repository.ReelsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReelsViewModel(
    private val repository: ReelsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReelsUiState>(
        ReelsUiState.Loading
    )

    val uiState: StateFlow<ReelsUiState> = _uiState.asStateFlow()

    fun onAction(action: ReelsAction) {
        when (action) {
            ReelsAction.Load -> loadReels()

            is ReelsAction.SaveReel -> {
                repository.saveReel(action.reel)
                loadReels()
            }
        }
    }

    private fun loadReels() {
        _uiState.value = ReelsUiState.Success(
            reels = repository.getReels()
        )
    }
}
