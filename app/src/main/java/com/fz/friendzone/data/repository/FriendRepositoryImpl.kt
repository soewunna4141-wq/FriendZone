package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Friend
import com.fz.friendzone.data.local.FriendLocalDataSource

class FriendRepositoryImpl(
    private val localDataSource: FriendLocalDataSource
) : FriendRepository {

    override fun getFriends(userId: String): List<Friend> {
        return localDataSource.getFriends(userId)
    }

    override fun saveFriend(friend: Friend) {
        localDataSource.saveFriend(friend)
    }
}
