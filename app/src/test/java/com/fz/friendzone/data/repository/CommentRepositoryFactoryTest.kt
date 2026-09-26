package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Comment
import org.junit.Assert.assertEquals
import org.junit.Test

class CommentRepositoryFactoryTest {

    @Test
    fun create_returnsRepositoryThatCanSaveAndReadComments() {
        val repository = CommentRepositoryFactory.create()

        val comment = Comment(
            id = "comment-1",
            postId = "post-1",
            userId = "user-1",
            text = "Hello FriendZone"
        )

        repository.saveComment(comment)

        assertEquals(
            listOf(comment),
            repository.getComments("post-1")
        )
    }
}
