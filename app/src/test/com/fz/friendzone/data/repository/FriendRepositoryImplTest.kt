package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Friend
import com.fz.friendzone.data.local.FriendLocalDataSource
import org.junit.Assert.assertEquals
import org.junit.Test

class FriendRepositoryImplTest {

    @Test
    fun getFriends_delegatesToLocalDataSource() {
        val friend = Friend(
            id = "1",
            userId = "user-1",
            friendUserId = "user-2"
        )

        val localDataSource = FakeFriendLocalDataSource(
            friends = listOf(friend)
        )
        val repository = FriendRepositoryImpl(localDataSource)

        assertEquals(
            listOf(friend),
            repository.getFriends("user-1")
        )
    }

    @Test
    fun saveFriend_delegatesToLocalDataSource() {
        val friend = Friend(
            id = "1",
            userId = "user-1",
            friendUserId = "user-2"
        )

        val localDataSource = FakeFriendLocalDataSource()
        val repository = FriendRepositoryImpl(localDataSource)

        repository.saveFriend(friend)

        assertEquals(friend, localDataSource.savedFriend)
    }

    private class FakeFriendLocalDataSource(
        private val friends: List<Friend> = emptyList()
    ) : FriendLocalDataSource {

        var savedFriend: Friend? = null

        override fun getFriends(userId: String): List<Friend> {
            return friends
        }

        override fun saveFriend(friend: Friend) {
            savedFriend = friend
        }
    }
}
