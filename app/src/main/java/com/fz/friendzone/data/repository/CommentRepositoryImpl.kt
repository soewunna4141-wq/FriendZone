package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Comment
import com.fz.friendzone.data.local.CommentLocalDataSource

class CommentRepositoryImpl(
    private val localDataSource: CommentLocalDataSource
) : CommentRepository {

    override fun getComments(postId: String): List<Comment> {
        return localDataSource.getComments(postId)
    }

    override fun saveComment(comment: Comment) {
        localDataSource.saveComment(comment)
    }
}
