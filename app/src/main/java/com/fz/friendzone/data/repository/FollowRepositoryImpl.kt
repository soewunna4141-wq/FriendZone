package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Follow
import com.fz.friendzone.data.local.FollowLocalDataSource

class FollowRepositoryImpl(
    private val localDataSource: FollowLocalDataSource
) : FollowRepository {

    override fun getFollowing(followerId: String): List<Follow> {
        return localDataSource.getFollowing(followerId)
    }

    override fun getFollowers(followingId: String): List<Follow> {
        return localDataSource.getFollowers(followingId)
    }

    override fun saveFollow(follow: Follow) {
        localDataSource.saveFollow(follow)
    }
}
