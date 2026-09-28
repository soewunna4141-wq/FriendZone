package com.fz.friendzone.data.repository

import com.fz.friendzone.data.local.InMemoryFriendLocalDataSource

object FriendRepositoryFactory {

    fun create(): FriendRepository {
        return FriendRepositoryImpl(
            localDataSource = InMemoryFriendLocalDataSource()
        )
    }
}
