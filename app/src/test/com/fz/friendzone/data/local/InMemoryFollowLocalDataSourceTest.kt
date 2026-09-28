package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Follow
import org.junit.Assert.assertEquals
import org.junit.Test

class InMemoryFollowLocalDataSourceTest {

    @Test
    fun getFollowing_returnsOnlyMatchingFollower() {
        val dataSource = InMemoryFollowLocalDataSource()

        val firstFollow = Follow(
            id = "1",
            followerId = "user-1",
            followingId = "user-2"
        )
        val secondFollow = Follow(
            id = "2",
            followerId = "user-1",
            followingId = "user-3"
        )
        val otherFollow = Follow(
            id = "3",
            followerId = "user-4",
            followingId = "user-2"
        )

        dataSource.saveFollow(firstFollow)
        dataSource.saveFollow(secondFollow)
        dataSource.saveFollow(otherFollow)

        assertEquals(
            listOf(firstFollow, secondFollow),
            dataSource.getFollowing("user-1")
        )
    }

    @Test
    fun getFollowers_returnsOnlyMatchingFollowingUser() {
        val dataSource = InMemoryFollowLocalDataSource()

        val firstFollow = Follow(
            id = "1",
            followerId = "user-1",
            followingId = "user-2"
        )
        val secondFollow = Follow(
            id = "2",
            followerId = "user-3",
            followingId = "user-2"
        )
        val otherFollow = Follow(
            id = "3",
            followerId = "user-4",
            followingId = "user-5"
        )

        dataSource.saveFollow(firstFollow)
        dataSource.saveFollow(secondFollow)
        dataSource.saveFollow(otherFollow)

        assertEquals(
            listOf(firstFollow, secondFollow),
            dataSource.getFollowers("user-2")
        )
    }

    @Test
    fun saveFollow_storesFollow() {
        val dataSource = InMemoryFollowLocalDataSource()

        val follow = Follow(
            id = "1",
            followerId = "user-1",
            followingId = "user-2"
        )

        dataSource.saveFollow(follow)

        assertEquals(
            listOf(follow),
            dataSource.getFollowing("user-1")
        )
        assertEquals(
            listOf(follow),
            dataSource.getFollowers("user-2")
        )
    }
}
