package com.fz.friendzone.data.repository

import com.fz.friendzone.data.local.InMemoryNewsLocalDataSource
import com.fz.friendzone.data.local.NewsLocalDataSource

object NewsRepositoryFactory {

    fun create(
        localDataSource: NewsLocalDataSource =
            InMemoryNewsLocalDataSource()
    ): NewsRepository {
        return NewsRepositoryImpl(
            localDataSource = localDataSource
        )
    }
}
