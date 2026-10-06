package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Post
import com.fz.friendzone.data.local.NewsLocalDataSource

class NewsRepositoryImpl(
    private val localDataSource: NewsLocalDataSource
) : NewsRepository {

    override fun getPosts(): List<Post> {
        return localDataSource.getPosts()
    }

    override fun getBinPosts(): List<Post> {
        return localDataSource.getBinPosts()
    }

    override fun savePost(post: Post) {
        localDataSource.savePost(post)
    }

    override fun updatePost(post: Post) {
        localDataSource.updatePost(post)
    }

    override fun movePostToBin(postId: String) {
        localDataSource.movePostToBin(postId)
    }

    override fun restorePost(postId: String) {
        localDataSource.restorePost(postId)
    }

    override fun movePostToAsh(postId: String) {
        localDataSource.movePostToAsh(postId)
    }
}
