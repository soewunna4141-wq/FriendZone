package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Follow

class InMemoryFollowLocalDataSource : FollowLocalDataSource {

    private val follows = mutableListOf<Follow>()

    override fun getFollowing(followerId: String): List<Follow> {
        return follows.filter { follow ->
            follow.followerId == followerId
        }
    }

    override fun getFollowers(followingId: String): List<Follow> {
        return follows.filter { follow ->
            follow.followingId == followingId
        }
    }

    override fun saveFollow(follow: Follow) {
        follows.add(follow)
    }
}
