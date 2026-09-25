package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Test

class UserRepositoryFactoryTest {

    @Test
    fun `create returns user repository`() {
        val repository = UserRepositoryFactory.create()

        assertNotNull(repository)
    }

    @Test
    fun `create returns repository with no current user by default`() {
        val repository = UserRepositoryFactory.create()

        assertNull(repository.getCurrentUser())
    }

    @Test
    fun `create returns repository with no user for unknown id`() {
        val repository = UserRepositoryFactory.create()

        assertNull(repository.getUser("unknown-user-id"))
    }

    @Test
    fun `create returns repository that can save user`() {
        val user = User(
            id = "user-1",
            name = "Test User",
            username = "testuser"
        )

        val repository = UserRepositoryFactory.create()

        repository.saveUser(user)

        assertEquals(
            user,
            repository.getUser("user-1")
        )
    }
}
