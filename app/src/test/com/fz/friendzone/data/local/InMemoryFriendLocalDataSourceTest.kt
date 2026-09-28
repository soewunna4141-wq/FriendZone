package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Friend
import org.junit.Assert.assertEquals
import org.junit.Test

class InMemoryFriendLocalDataSourceTest {

    @Test
    fun getFriends_returnsMatchingUserRelationships() {
        val dataSource = InMemoryFriendLocalDataSource()

        val firstFriend = Friend(
            id = "1",
            userId = "user-1",
            friendUserId = "user-2"
        )
        val secondFriend = Friend(
            id = "2",
            userId = "user-3",
            friendUserId = "user-1"
        )
        val otherFriend = Friend(
            id = "3",
            userId = "user-4",
            friendUserId = "user-5"
        )

        dataSource.saveFriend(firstFriend)
        dataSource.saveFriend(secondFriend)
        dataSource.saveFriend(otherFriend)

        assertEquals(
            listOf(firstFriend, secondFriend),
            dataSource.getFriends("user-1")
        )
    }

    @Test
    fun getFriends_excludesUnrelatedRelationships() {
        val dataSource = InMemoryFriendLocalDataSource()

        val friend = Friend(
            id = "1",
            userId = "user-1",
            friendUserId = "user-2"
        )
        val otherFriend = Friend(
            id = "2",
            userId = "user-3",
            friendUserId = "user-4"
        )

        dataSource.saveFriend(friend)
        dataSource.saveFriend(otherFriend)

        assertEquals(
            emptyList<Friend>(),
            dataSource.getFriends("user-5")
        )
    }

    @Test
    fun saveFriend_storesFriend() {
        val dataSource = InMemoryFriendLocalDataSource()

        val friend = Friend(
            id = "1",
            userId = "user-1",
            friendUserId = "user-2"
        )

        dataSource.saveFriend(friend)

        assertEquals(
            listOf(friend),
            dataSource.getFriends("user-1")
        )
        assertEquals(
            listOf(friend),
            dataSource.getFriends("user-2")
        )
    }
}
