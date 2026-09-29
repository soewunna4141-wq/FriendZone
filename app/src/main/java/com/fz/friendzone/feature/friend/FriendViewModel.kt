package com.fz.friendzone.feature.friend

import androidx.lifecycle.ViewModel
import com.fz.friendzone.core.model.Friend
import com.fz.friendzone.data.repository.FriendRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FriendUiState(
    val friends: List<Friend> = emptyList()
)

sealed interface FriendAction {

    data class Load(
        val userId: String
    ) : FriendAction

    data class Save(
        val friend: Friend
    ) : FriendAction
}

class FriendViewModel(
    private val repository: FriendRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        FriendUiState()
    )

    val uiState: StateFlow<FriendUiState> =
        _uiState.asStateFlow()

    fun onAction(action: FriendAction) {
        when (action) {

            is FriendAction.Load -> {
                loadFriends(action.userId)
            }

            is FriendAction.Save -> {
                saveFriend(action.friend)
            }
        }
    }

    private fun loadFriends(userId: String) {
        val friends = repository.getFriends(userId)

        _uiState.value = FriendUiState(
            friends = friends
        )
    }

    private fun saveFriend(friend: Friend) {
        repository.saveFriend(friend)
        loadFriends(friend.userId)
    }
}
