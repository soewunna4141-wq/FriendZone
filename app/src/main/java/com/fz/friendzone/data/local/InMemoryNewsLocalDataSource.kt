package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Post

class InMemoryNewsLocalDataSource : NewsLocalDataSource {

    private val posts = mutableListOf<Post>()

    override fun getPosts(): List<Post> {
        return posts
            .sortedByDescending { post ->
                post.createdAt
            }
    }
}
