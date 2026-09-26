package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Comment
import org.junit.Assert.assertEquals
import org.junit.Test

class InMemoryCommentLocalDataSourceTest {

    @Test
    fun getComments_returnsOnlyCommentsForRequestedPost() {
        val dataSource = InMemoryCommentLocalDataSource()

        val firstComment = Comment(
            id = "comment-1",
            postId = "post-1",
            userId = "user-1",
            text = "First comment"
        )

        val secondComment = Comment(
            id = "comment-2",
            postId = "post-2",
            userId = "user-2",
            text = "Second comment"
        )

        dataSource.saveComment(firstComment)
        dataSource.saveComment(secondComment)

        val result = dataSource.getComments("post-1")

        assertEquals(listOf(firstComment), result)
    }

    @Test
    fun saveComment_storesComment() {
        val dataSource = InMemoryCommentLocalDataSource()

        val comment = Comment(
            id = "comment-1",
            postId = "post-1",
            userId = "user-1",
            text = "Hello FriendZone"
        )

        dataSource.saveComment(comment)

        assertEquals(
            listOf(comment),
            dataSource.getComments("post-1")
        )
    }

    @Test
    fun getComments_whenPostHasNoComments_returnsEmptyList() {
        val dataSource = InMemoryCommentLocalDataSource()

        val result = dataSource.getComments("post-1")

        assertEquals(emptyList<Comment>(), result)
    }
}
