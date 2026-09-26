package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Comment

interface CommentRepository {

    fun getComments(postId: String): List<Comment>

    fun saveComment(comment: Comment)
}
