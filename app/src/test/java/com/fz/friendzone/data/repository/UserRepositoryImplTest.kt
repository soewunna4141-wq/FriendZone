package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.User
import com.fz.friendzone.data.local.UserLocalDataSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UserRepositoryImplTest {

    @Test
    fun getCurrentUser_returnsUserFromLocalDataSource() {
        val user = User(
            id = "user-1",
            name = "Test User",
            username = "testuser"
        )

        val localDataSource = object : UserLocalDataSource {

            override fun getCurrentUser(): User? {
                return user
            }

            override fun getUser(userId: String): User? {
                return null
            }

            override fun saveUser(user: User) {
            }
        }

        val repository = UserRepositoryImpl(
            localDataSource = localDataSource
        )

        assertEquals(
            user,
            repository.getCurrentUser()
        )
    }

    @Test
    fun getCurrentUser_returnsNullWhenLocalDataSourceReturnsNull() {
        val localDataSource = object : UserLocalDataSource {

            override fun getCurrentUser(): User? {
                return null
            }

            override fun getUser(userId: String): User? {
                return null
            }

            override fun saveUser(user: User) {
            }
        }

        val repository = UserRepositoryImpl(
            localDataSource = localDataSource
        )

        assertNull(
            repository.getCurrentUser()
        )
    }

    @Test
    fun getUser_returnsUserFromLocalDataSource() {
        val user = User(
            id = "user-1",
            name = "Test User",
            username = "testuser"
        )

        val localDataSource = object : UserLocalDataSource {

            override fun getCurrentUser(): User? {
                return null
            }

            override fun getUser(userId: String): User? {
                return if (userId == user.id) user else null
            }

            override fun saveUser(user: User) {
            }
        }

        val repository = UserRepositoryImpl(
            localDataSource = localDataSource
        )

        assertEquals(
            user,
            repository.getUser("user-1")
        )
    }

    @Test
    fun getUser_returnsNullWhenLocalDataSourceReturnsNull() {
        val localDataSource = object : UserLocalDataSource {

            override fun getCurrentUser(): User? {
                return null
            }

            override fun getUser(userId: String): User? {
                return null
            }

            override fun saveUser(user: User) {
            }
        }

        val repository = UserRepositoryImpl(
            localDataSource = localDataSource
        )

        assertNull(
            repository.getUser("user-1")
        )
    }

    @Test
    fun saveUser_delegatesToLocalDataSource() {
        val user = User(
            id = "user-1",
            name = "Test User",
            username = "testuser"
        )

        var savedUser: User? = null

        val localDataSource = object : UserLocalDataSource {

            override fun getCurrentUser(): User? {
                return null
            }

            override fun getUser(userId: String): User? {
                return null
            }

            override fun saveUser(user: User) {
                savedUser = user
            }
        }

        val repository = UserRepositoryImpl(
            localDataSource = localDataSource
        )

        repository.saveUser(user)

        assertEquals(
            user,
            savedUser
        )
    }
}
