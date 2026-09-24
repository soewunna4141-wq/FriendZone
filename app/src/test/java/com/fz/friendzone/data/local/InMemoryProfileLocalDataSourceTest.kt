package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Profile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class InMemoryProfileLocalDataSourceTest {

    @Test
    fun getProfileByUserId_returnsMatchingProfile() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        val dataSource = InMemoryProfileLocalDataSource()

        dataSource.saveProfile(profile)

        assertEquals(
            profile,
            dataSource.getProfile("user-1")
        )
    }

    @Test
    fun getProfileByUserId_whenUserIdDoesNotMatch_returnsNull() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        val dataSource = InMemoryProfileLocalDataSource()

        dataSource.saveProfile(profile)

        assertNull(
            dataSource.getProfile("user-2")
        )
    }
}
