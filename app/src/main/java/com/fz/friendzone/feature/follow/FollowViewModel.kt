package com.fz.friendzone.feature.follow

import androidx.lifecycle.ViewModel
import com.fz.friendzone.core.model.Follow
import com.fz.friendzone.data.repository.FollowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FollowUiState(
    val followers: List<Follow> = emptyList(),
    val following: List<Follow> = emptyList()
)

sealed interface FollowAction {

    data class Load(
        val userId: String
    ) : FollowAction

    data class Save(
        val follow: Follow
    ) : FollowAction
}

class FollowViewModel(
    private val repository: FollowRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        FollowUiState()
    )

    val uiState: StateFlow<FollowUiState> =
        _uiState.asStateFlow()

    fun onAction(action: FollowAction) {
        when (action) {

            is FollowAction.Load -> {
                loadFollow(action.userId)
            }

            is FollowAction.Save -> {
                saveFollow(action.follow)
            }
        }
    }

    private fun loadFollow(userId: String) {
        val followers = repository.getFollowers(userId)
        val following = repository.getFollowing(userId)

        _uiState.value = FollowUiState(
            followers = followers,
            following = following
        )
    }

    private fun saveFollow(follow: Follow) {
        repository.saveFollow(follow)
        loadFollow(follow.followerId)
    }
}
