package com.fz.friendzone.data.repository

import org.junit.Assert.assertNotNull
import org.junit.Test

class FriendRepositoryFactoryTest {

    @Test
    fun create_returnsFriendRepository() {
        val repository = FriendRepositoryFactory.create()

        assertNotNull(repository)
    }
}
