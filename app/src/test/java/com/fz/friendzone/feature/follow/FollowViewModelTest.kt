package com.fz.friendzone.feature.follow

import com.fz.friendzone.core.model.Follow
import com.fz.friendzone.data.repository.FollowRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class FollowViewModelTest {

    private class FakeFollowRepository : FollowRepository {

        val savedFollows = mutableListOf<Follow>()

        private val follows = mutableListOf<Follow>()

        override fun getFollowing(
            followerId: String
        ): List<Follow> {
            return follows.filter { it.followerId == followerId }
        }

        override fun getFollowers(
            followingId: String
        ): List<Follow> {
            return follows.filter { it.followingId == followingId }
        }

        override fun saveFollow(follow: Follow) {
            follows.add(follow)
            savedFollows.add(follow)
        }
    }

    @Test
    fun load_populatesFollowersAndFollowing() {
        val repository = FakeFollowRepository()

        val following = Follow(
            id = "follow-1",
            followerId = "user-1",
            followingId = "user-2"
        )

        val follower = Follow(
            id = "follow-2",
            followerId = "user-3",
            followingId = "user-1"
        )

        repository.saveFollow(following)
        repository.saveFollow(follower)

        val viewModel = FollowViewModel(repository)

        viewModel.onAction(
            FollowAction.Load("user-1")
        )

        assertEquals(
            listOf(follower),
            viewModel.uiState.value.followers
        )

        assertEquals(
            listOf(following),
            viewModel.uiState.value.following
        )
    }

    @Test
    fun save_savesFollowAndReloadsFollowing() {
        val repository = FakeFollowRepository()
        val viewModel = FollowViewModel(repository)

        val follow = Follow(
            id = "follow-1",
            followerId = "user-1",
            followingId = "user-2"
        )

        viewModel.onAction(
            FollowAction.Save(follow)
        )

        assertEquals(
            listOf(follow),
            repository.savedFollows
        )

        assertEquals(
            listOf(follow),
            viewModel.uiState.value.following
        )
    }
}
