package com.fz.friendzone.data.repository

import com.fz.friendzone.data.local.InMemoryAccountLocalDataSource

object AccountRepositoryFactory {

    fun create(): AccountRepository {
        val localDataSource = InMemoryAccountLocalDataSource()

        return AccountRepositoryImpl(
            localDataSource = localDataSource
        )
    }
}
