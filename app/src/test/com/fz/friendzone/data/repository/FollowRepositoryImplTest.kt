package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Follow
import com.fz.friendzone.data.local.FollowLocalDataSource
import org.junit.Assert.assertEquals
import org.junit.Test

class FollowRepositoryImplTest {

    @Test
    fun getFollowing_delegatesToLocalDataSource() {
        val follow = Follow(
            id = "1",
            followerId = "user-1",
            followingId = "user-2"
        )

        val localDataSource = FakeFollowLocalDataSource(
            following = listOf(follow)
        )
        val repository = FollowRepositoryImpl(localDataSource)

        assertEquals(
            listOf(follow),
            repository.getFollowing("user-1")
        )
    }

    @Test
    fun getFollowers_delegatesToLocalDataSource() {
        val follow = Follow(
            id = "1",
            followerId = "user-1",
            followingId = "user-2"
        )

        val localDataSource = FakeFollowLocalDataSource(
            followers = listOf(follow)
        )
        val repository = FollowRepositoryImpl(localDataSource)

        assertEquals(
            listOf(follow),
            repository.getFollowers("user-2")
        )
    }

    @Test
    fun saveFollow_delegatesToLocalDataSource() {
        val follow = Follow(
            id = "1",
            followerId = "user-1",
            followingId = "user-2"
        )

        val localDataSource = FakeFollowLocalDataSource()
        val repository = FollowRepositoryImpl(localDataSource)

        repository.saveFollow(follow)

        assertEquals(follow, localDataSource.savedFollow)
    }

    private class FakeFollowLocalDataSource(
        private val following: List<Follow> = emptyList(),
        private val followers: List<Follow> = emptyList()
    ) : FollowLocalDataSource {

        var savedFollow: Follow? = null

        override fun getFollowing(followerId: String): List<Follow> {
            return following
        }

        override fun getFollowers(followingId: String): List<Follow> {
            return followers
        }

        override fun saveFollow(follow: Follow) {
            savedFollow = follow
        }
    }
}
