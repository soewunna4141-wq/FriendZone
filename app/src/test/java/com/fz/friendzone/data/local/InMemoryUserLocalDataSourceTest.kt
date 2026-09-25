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
}
