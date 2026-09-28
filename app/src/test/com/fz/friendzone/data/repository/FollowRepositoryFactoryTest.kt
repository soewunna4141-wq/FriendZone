package com.fz.friendzone.data.repository

import org.junit.Assert.assertNotNull
import org.junit.Test

class FollowRepositoryFactoryTest {

    @Test
    fun create_returnsFollowRepository() {
        val repository = FollowRepositoryFactory.create()

        assertNotNull(repository)
    }
}
