package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.User

class InMemoryUserLocalDataSource(
    private var currentUser: User? = null
) : UserLocalDataSource {

    private val users = mutableMapOf<String, User>()

    init {
        currentUser?.let { user ->
            users[user.id] = user
        }
    }

    override fun getCurrentUser(): User? {
        return currentUser
    }

    override fun getUser(userId: String): User? {
        return users[userId]
    }

    override fun saveUser(user: User) {
        users[user.id] = user
    }
}
