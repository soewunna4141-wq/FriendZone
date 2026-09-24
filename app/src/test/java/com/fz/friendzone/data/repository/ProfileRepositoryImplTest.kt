package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.data.local.ProfileLocalDataSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProfileRepositoryImplTest {

    @Test
    fun getProfile_returnsProfileFromLocalDataSource() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        val dataSource = object : ProfileLocalDataSource {
            override fun getProfile(): Profile? {
                return profile
            }

            override fun saveProfile(profile: Profile) {
            }
        }

        val repository = ProfileRepositoryImpl(
            localDataSource = dataSource
        )

        assertEquals(
            profile,
            repository.getProfile()
        )
    }

    @Test
    fun getProfileByUserId_returnsMatchingProfile() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        val dataSource = object : ProfileLocalDataSource {
            override fun getProfile(): Profile? {
                return profile
            }

            override fun getProfile(userId: String): Profile? {
                return super.getProfile(userId)
            }

            override fun saveProfile(profile: Profile) {
            }
        }

        val repository = ProfileRepositoryImpl(
            localDataSource = dataSource
        )

        assertEquals(
            profile,
            repository.getProfile("user-1")
        )
    }

    @Test
    fun getProfileByUserId_whenUserIdDoesNotMatch_returnsNull() {
        val profile = Profile(
            userId = "user-1",
            displayName = "Test User"
        )

        val dataSource = object : ProfileLocalDataSource {
            override fun getProfile(): Profile? {
                return profile
            }

            override fun getProfile(userId: String): Profile? {
                return super.getProfile(userId)
            }

            override fun saveProfile(profile: Profile) {
            }
        }

        val repository = ProfileRepositoryImpl(
            localDataSource = dataSource
        )

        assertNull(
            repository.getProfile("user-2")
        )
    }
}
