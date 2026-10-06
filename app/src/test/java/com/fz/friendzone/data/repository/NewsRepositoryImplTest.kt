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

            override fun getBinPosts(): List<Post> {
                return emptyList()
            }

            override fun savePost(post: Post) {
            }

            override fun updatePost(post: Post) {
            }

            override fun movePostToBin(postId: String) {
            }

            override fun restorePost(postId: String) {
            }

            override fun movePostToAsh(postId: String) {
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

            override fun getBinPosts(): List<Post> {
                return emptyList()
            }

            override fun savePost(post: Post) {
                savedPosts.add(post)
            }

            override fun updatePost(post: Post) {
            }

            override fun movePostToBin(postId: String) {
            }

            override fun restorePost(postId: String) {
            }

            override fun movePostToAsh(postId: String) {
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
