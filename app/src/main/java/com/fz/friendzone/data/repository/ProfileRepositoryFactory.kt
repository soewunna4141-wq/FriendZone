package com.fz.friendzone.data.repository

import com.fz.friendzone.data.local.InMemoryProfileLocalDataSource

object ProfileRepositoryFactory {

    fun create(): ProfileRepository {
        val localDataSource = InMemoryProfileLocalDataSource()

        return ProfileRepositoryImpl(
            localDataSource = localDataSource
        )
    }
}
