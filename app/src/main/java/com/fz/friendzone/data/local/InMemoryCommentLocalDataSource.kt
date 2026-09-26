package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Comment

class InMemoryCommentLocalDataSource : CommentLocalDataSource {

    private val comments = mutableListOf<Comment>()

    override fun getComments(postId: String): List<Comment> {
        return comments.filter { comment ->
            comment.postId == postId
        }
    }

    override fun saveComment(comment: Comment) {
        comments.add(comment)
    }
}
