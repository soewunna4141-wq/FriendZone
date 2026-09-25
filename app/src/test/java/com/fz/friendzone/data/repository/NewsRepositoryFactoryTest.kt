package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Post
import org.junit.Assert.assertEquals
import org.junit.Test

class NewsRepositoryFactoryTest {

    @Test
    fun create_returnsRepositoryThatPersistsPosts() {
        val repository = NewsRepositoryFactory.create()

        val post = Post(
            id = "post-1",
            userId = "user-1",
            caption = "Factory test post"
        )

        repository.savePost(post)

        assertEquals(
            listOf(post),
            repository.getPosts()
        )
    }
}
