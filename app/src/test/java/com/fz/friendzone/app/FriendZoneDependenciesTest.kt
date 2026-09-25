package com.fz.friendzone.app

import com.fz.friendzone.data.repository.UserRepository
import org.junit.Assert.assertNotNull
import org.junit.Test

class FriendZoneDependenciesTest {

    @Test
    fun userRepository_isAvailableFromApplicationDependencies() {
        val dependencies = FriendZoneDependencies()

        assertNotNull(dependencies.userRepository)
    }

    @Test
    fun userRepository_returnsRepositoryFromApplicationDependencies() {
        val dependencies = FriendZoneDependencies()

        val repository = dependencies.userRepository

        assertNotNull(repository)
        assert(repository is UserRepository)
    }
}
