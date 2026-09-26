package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Comment

interface CommentLocalDataSource {

    fun getComments(postId: String): List<Comment>

    fun saveComment(comment: Comment)
}
