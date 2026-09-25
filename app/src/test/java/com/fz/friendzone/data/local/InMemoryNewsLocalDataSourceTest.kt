package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Post
import org.junit.Assert.assertEquals
import org.junit.Test

class InMemoryNewsLocalDataSourceTest {

    @Test
    fun getPosts_returnsPostsOrderedByCreatedAtDescending() {
        val olderPost = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Older post",
            createdAt = 1000L
        )

        val newerPost = Post(
            id = "post-2",
            userId = "user-2",
            caption = "Newer post",
            createdAt = 2000L
        )

        val dataSource = InMemoryNewsLocalDataSource()

        dataSource.savePost(olderPost)
        dataSource.savePost(newerPost)

        val posts = dataSource.getPosts()

        assertEquals(
            listOf(newerPost, olderPost),
            posts
        )
    }
}
