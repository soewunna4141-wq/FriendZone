package com.fz.friendzone.data.repository

import com.fz.friendzone.data.local.InMemoryNewsLocalDataSource
import com.fz.friendzone.feature.news.NewsViewModel

object NewsRepositoryFactory {

    fun create(): NewsRepository {
        return NewsRepositoryImpl(
            localDataSource = InMemoryNewsLocalDataSource()
        )
    }

    fun createViewModel(): NewsViewModel {
        return NewsViewModel(
            repository = create()
        )
    }
}
