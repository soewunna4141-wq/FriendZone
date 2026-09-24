package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Post
import com.fz.friendzone.data.local.NewsLocalDataSource
import org.junit.Assert.assertEquals
import org.junit.Test

class NewsRepositoryImplTest {

    @Test
    fun getPosts_returnsPostsFromLocalDataSource() {
        val posts = listOf(
            Post(
                id = "post-1",
                userId = "user-1",
                caption = "Test post"
            )
        )

        val dataSource = object : NewsLocalDataSource {
            override fun getPosts(): List<Post> {
                return posts
            }
        }

        val repository = NewsRepositoryImpl(
            localDataSource = dataSource
        )

        val result = repository.getPosts()

        assertEquals(posts, result)
    }
}
