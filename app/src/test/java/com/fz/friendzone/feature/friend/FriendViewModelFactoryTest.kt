package com.fz.friendzone.feature.friend

import com.fz.friendzone.data.repository.FriendRepository
import org.junit.Assert.assertTrue
import org.junit.Test

class FriendViewModelFactoryTest {

    private class FakeFriendRepository : FriendRepository {

        override fun getFriends(
            userId: String
        ) = emptyList<com.fz.friendzone.core.model.Friend>()

        override fun saveFriend(
            friend: com.fz.friendzone.core.model.Friend
        ) = Unit
    }

    @Test
    fun create_returnsFriendViewModel() {
        val repository = FakeFriendRepository()
        val factory = FriendViewModelFactory(repository)

        val viewModel = factory.create(
            FriendViewModel::class.java
        )

        assertTrue(
            viewModel is FriendViewModel
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun create_unknownViewModelType_throwsException() {
        val repository = FakeFriendRepository()
        val factory = FriendViewModelFactory(repository)

        factory.create(
            UnknownViewModel::class.java
        )
    }

    private class UnknownViewModel : androidx.lifecycle.ViewModel()
}
