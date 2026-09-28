package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Friend

interface FriendLocalDataSource {

    fun getFriends(userId: String): List<Friend>

    fun saveFriend(friend: Friend)
}
