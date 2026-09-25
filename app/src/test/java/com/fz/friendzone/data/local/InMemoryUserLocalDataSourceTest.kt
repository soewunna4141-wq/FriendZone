package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class InMemoryUserLocalDataSourceTest {

    @Test
    fun getCurrentUser_returnsNullWhenNoUserExists() {
        val dataSource = InMemoryUserLocalDataSource()

        assertNull(
            dataSource.getCurrentUser()
        )
    }

    @Test
    fun getUser_returnsNullWhenNoUserExists() {
        val dataSource = InMemoryUserLocalDataSource()

        assertNull(
            dataSource.getUser("user-1")
        )
    }

    @Test
    fun getCurrentUser_returnsConfiguredUser() {
        val user = User(
            id = "user-1",
            name = "Test User",
            username = "testuser"
        )

        val dataSource = InMemoryUserLocalDataSource(
            currentUser = user
        )

        assertEquals(
            user,
            dataSource.getCurrentUser()
        )
    }

    @Test
    fun getUser_returnsCurrentUserWhenIdMatches() {
        val user = User(
            id = "user-1",
            name = "Test User",
            username = "testuser"
        )

        val dataSource = InMemoryUserLocalDataSource(
            currentUser = user
        )

        assertEquals(
            user,
            dataSource.getUser("user-1")
        )
    }

    @Test
    fun getUser_returnsNullWhenIdDoesNotMatch() {
        val user = User(
            id = "user-1",
            name = "Test User",
            username = "testuser"
        )

        val dataSource = InMemoryUserLocalDataSource(
            currentUser = user
        )

        assertNull(
            dataSource.getUser("user-2")
        )
    }

    @Test
    fun saveUser_storesUserById() {
        val user = User(
            id = "user-2",
            name = "Saved User",
            username = "saveduser"
        )

        val dataSource = InMemoryUserLocalDataSource()

        dataSource.saveUser(user)

        assertEquals(
            user,
            dataSource.getUser("user-2")
        )
    }

    @Test
    fun saveUser_updatesExistingUserWithSameId() {
        val originalUser = User(
            id = "user-1",
            name = "Original User",
            username = "original"
        )

        val updatedUser = User(
            id = "user-1",
            name = "Updated User",
            username = "updated"
        )

        val dataSource = InMemoryUserLocalDataSource(
            currentUser = originalUser
        )

        dataSource.saveUser(updatedUser)

        assertEquals(
            updatedUser,
            dataSource.getUser("user-1")
        )
    }
}
