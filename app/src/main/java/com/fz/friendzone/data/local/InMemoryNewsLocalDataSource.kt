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

    override fun savePost(post: Post) {
        posts.add(post)
    }

    override fun updatePost(post: Post) {
        val index = posts.indexOfFirst { existingPost ->
            existingPost.id == post.id
        }

        if (index >= 0) {
            posts[index] = post
        }
    }
}
