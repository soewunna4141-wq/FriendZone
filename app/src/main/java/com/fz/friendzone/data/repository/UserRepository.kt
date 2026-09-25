package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.User

interface UserRepository {

    fun getCurrentUser(): User?

    fun getUser(userId: String): User?

    fun saveUser(user: User)
}
