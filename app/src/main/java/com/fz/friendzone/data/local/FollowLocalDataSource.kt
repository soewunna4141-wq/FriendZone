package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Follow

interface FollowLocalDataSource {

    fun getFollowing(followerId: String): List<Follow>

    fun getFollowers(followingId: String): List<Follow>

    fun saveFollow(follow: Follow)
}
