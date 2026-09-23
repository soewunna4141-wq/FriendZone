package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Post
import com.fz.friendzone.data.local.NewsLocalDataSource

class NewsRepositoryImpl(
    private val localDataSource: NewsLocalDataSource
) : NewsRepository {

    override fun getPosts(): List<Post> {
        return localDataSource.getPosts()
    }
}
