package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Comment
import com.fz.friendzone.data.local.CommentLocalDataSource
import org.junit.Assert.assertEquals
import org.junit.Test

class CommentRepositoryImplTest {

    @Test
    fun getComments_delegatesToLocalDataSource() {
        val comment = Comment(
            id = "comment-1",
            postId = "post-1",
            userId = "user-1",
            text = "Hello FriendZone"
        )

        val localDataSource = FakeCommentLocalDataSource(
            comments = listOf(comment)
        )
        val repository = CommentRepositoryImpl(localDataSource)

        val result = repository.getComments("post-1")

        assertEquals(listOf(comment), result)
    }

    @Test
    fun saveComment_delegatesToLocalDataSource() {
        val localDataSource = FakeCommentLocalDataSource()
        val repository = CommentRepositoryImpl(localDataSource)

        val comment = Comment(
            id = "comment-1",
            postId = "post-1",
            userId = "user-1",
            text = "Hello FriendZone"
        )

        repository.saveComment(comment)

        assertEquals(
            listOf(comment),
            localDataSource.savedComments
        )
    }

    private class FakeCommentLocalDataSource(
        private val comments: List<Comment> = emptyList()
    ) : CommentLocalDataSource {

        val savedComments = mutableListOf<Comment>()

        override fun getComments(postId: String): List<Comment> {
            return comments.filter { it.postId == postId }
        }

        override fun saveComment(comment: Comment) {
            savedComments.add(comment)
        }
    }
}
