package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Profile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProfileRepositoryFactoryTest {

    @Test
    fun create_returnsRepositoryThatCanLookupProfileByUserId() {
        val repository = ProfileRepositoryFactory.create()

        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        repository.saveProfile(profile)

        assertEquals(
            profile,
            repository.getProfile("user-1")
        )
    }

    @Test
    fun create_returnsRepositoryThatReturnsNullForUnknownUserId() {
        val repository = ProfileRepositoryFactory.create()

        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        repository.saveProfile(profile)

        assertNull(
            repository.getProfile("user-2")
        )
    }
}
