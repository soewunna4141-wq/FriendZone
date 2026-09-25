package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.User

class InMemoryUserLocalDataSource : UserLocalDataSource {

    private var currentUser: User? = null

    private val users = mutableMapOf<String, User>()

    override fun getCurrentUser(): User? {
        return currentUser
    }

    override fun getUser(userId: String): User? {
        return users[userId]
    }
}
