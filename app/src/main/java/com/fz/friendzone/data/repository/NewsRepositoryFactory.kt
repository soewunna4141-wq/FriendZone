package com.fz.friendzone.data.repository

import com.fz.friendzone.data.local.InMemoryNewsLocalDataSource

object NewsRepositoryFactory {

    fun create(): NewsRepository {
        return NewsRepositoryImpl(
            localDataSource = InMemoryNewsLocalDataSource()
        )
    }
}
