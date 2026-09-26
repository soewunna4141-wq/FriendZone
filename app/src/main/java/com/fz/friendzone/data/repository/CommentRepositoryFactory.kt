package com.fz.friendzone.data.repository

import com.fz.friendzone.data.local.InMemoryCommentLocalDataSource

object CommentRepositoryFactory {

    fun create(): CommentRepository {
        return CommentRepositoryImpl(
            localDataSource = InMemoryCommentLocalDataSource()
        )
    }
}
