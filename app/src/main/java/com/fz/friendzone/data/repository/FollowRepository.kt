package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Follow

interface FollowRepository {

    fun getFollowing(followerId: String): List<Follow>

    fun getFollowers(followingId: String): List<Follow>

    fun saveFollow(follow: Follow)
}
