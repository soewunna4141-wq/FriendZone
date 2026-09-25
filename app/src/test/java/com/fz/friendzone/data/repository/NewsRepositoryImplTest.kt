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

            override fun savePost(post: Post) {
            }
        }

        val repository = NewsRepositoryImpl(
            localDataSource = dataSource
        )

        val result = repository.getPosts()

        assertEquals(posts, result)
    }

    @Test
    fun savePost_delegatesPostToLocalDataSource() {
        val savedPosts = mutableListOf<Post>()

        val dataSource = object : NewsLocalDataSource {
            override fun getPosts(): List<Post> {
                return savedPosts
            }

            override fun savePost(post: Post) {
                savedPosts.add(post)
            }
        }

        val repository = NewsRepositoryImpl(
            localDataSource = dataSource
        )

        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Saved post"
        )

        repository.savePost(post)

        assertEquals(
            listOf(post),
            savedPosts
        )
    }
}
