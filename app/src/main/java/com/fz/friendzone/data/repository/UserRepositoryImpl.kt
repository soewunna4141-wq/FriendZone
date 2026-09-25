package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.User
import com.fz.friendzone.data.local.UserLocalDataSource

class UserRepositoryImpl(
    private val localDataSource: UserLocalDataSource
) : UserRepository {

    override fun getCurrentUser(): User? {
        return localDataSource.getCurrentUser()
    }

    override fun getUser(userId: String): User? {
        return localDataSource.getUser(userId)
    }

    override fun saveUser(user: User) {
        localDataSource.saveUser(user)
    }
}
