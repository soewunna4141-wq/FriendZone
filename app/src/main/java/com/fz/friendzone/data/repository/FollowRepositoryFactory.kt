package com.fz.friendzone.data.repository

import com.fz.friendzone.data.local.InMemoryFollowLocalDataSource

object FollowRepositoryFactory {

    fun create(): FollowRepository {
        return FollowRepositoryImpl(
            localDataSource = InMemoryFollowLocalDataSource()
        )
    }
}
