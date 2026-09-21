package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.data.local.FakeProfileLocalDataSource
import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileRepositoryImplTest {

    @Test
    fun saveProfile_thenGetProfile_returnsSavedProfile() {
        val dataSource = FakeProfileLocalDataSource()
        val repository = ProfileRepositoryImpl(
            localDataSource = dataSource
        )

        val profile = Profile(
            userId = "test-user",
            displayName = "Test User",
            bio = "Test Bio"
        )

        repository.saveProfile(profile)

        val result = repository.getProfile()

        assertEquals(profile, result)
    }
}
