package com.fz.friendzone.data.repository

import com.fz.friendzone.data.local.InMemoryUserLocalDataSource

object UserRepositoryFactory {

    fun create(): UserRepository {
        return UserRepositoryImpl(
            localDataSource = InMemoryUserLocalDataSource()
        )
    }
}
