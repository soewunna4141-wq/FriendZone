package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Friend

interface FriendRepository {

    fun getFriends(userId: String): List<Friend>

    fun saveFriend(friend: Friend)
}
