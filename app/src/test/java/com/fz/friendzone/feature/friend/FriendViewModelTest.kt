package com.fz.friendzone.feature.friend

import com.fz.friendzone.core.model.Friend
import com.fz.friendzone.data.repository.FriendRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class FriendViewModelTest {

    private class FakeFriendRepository : FriendRepository {

        val savedFriends = mutableListOf<Friend>()

        private val friends = mutableListOf<Friend>()

        override fun getFriends(
            userId: String
        ): List<Friend> {
            return friends.filter { friend ->
                friend.userId == userId ||
                    friend.friendUserId == userId
            }
        }

        override fun saveFriend(friend: Friend) {
            friends.add(friend)
            savedFriends.add(friend)
        }
    }

    @Test
    fun load_populatesFriends() {
        val repository = FakeFriendRepository()

        val friend = Friend(
            id = "friend-1",
            userId = "user-1",
            friendUserId = "user-2"
        )

        repository.saveFriend(friend)

        val viewModel = FriendViewModel(repository)

        viewModel.onAction(
            FriendAction.Load("user-1")
        )

        assertEquals(
            listOf(friend),
            viewModel.uiState.value.friends
        )
    }

    @Test
    fun save_savesFriendAndReloadsFriends() {
        val repository = FakeFriendRepository()
        val viewModel = FriendViewModel(repository)

        val friend = Friend(
            id = "friend-1",
            userId = "user-1",
            friendUserId = "user-2"
        )

        viewModel.onAction(
            FriendAction.Save(friend)
        )

        assertEquals(
            listOf(friend),
            repository.savedFriends
        )

        assertEquals(
            listOf(friend),
            viewModel.uiState.value.friends
        )
    }
}
