package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Friend

class InMemoryFriendLocalDataSource : FriendLocalDataSource {

    private val friends = mutableListOf<Friend>()

    override fun getFriends(userId: String): List<Friend> {
        return friends.filter { friend ->
            friend.userId == userId || friend.friendUserId == userId
        }
    }

    override fun saveFriend(friend: Friend) {
        friends.add(friend)
    }
}
