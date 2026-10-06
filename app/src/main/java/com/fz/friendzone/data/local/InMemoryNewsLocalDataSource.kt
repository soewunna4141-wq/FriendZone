package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.PostLifecycleState

class InMemoryNewsLocalDataSource : NewsLocalDataSource {

    private val posts = mutableListOf<Post>()

    override fun getPosts(): List<Post> {
        return posts
            .filter { post ->
                post.lifecycleState == PostLifecycleState.ACTIVE
            }
            .sortedByDescending { post ->
                post.createdAt
            }
    }

    override fun getBinPosts(): List<Post> {
        return posts
            .filter { post ->
                post.lifecycleState == PostLifecycleState.BIN
            }
            .sortedByDescending { post ->
                post.deletedAt ?: post.createdAt
            }
    }

    override fun savePost(post: Post) {
        posts.add(
            post.copy(
                lifecycleState = PostLifecycleState.ACTIVE,
                deletedAt = null
            )
        )
    }

    override fun updatePost(post: Post) {
        val index = posts.indexOfFirst { existingPost ->
            existingPost.id == post.id
        }

        if (index >= 0) {
            posts[index] = post
        }
    }

    override fun movePostToBin(postId: String) {
        val index = posts.indexOfFirst { post ->
            post.id == postId
        }

        if (index >= 0) {
            val existingPost = posts[index]

            if (existingPost.lifecycleState == PostLifecycleState.ACTIVE) {
                posts[index] = existingPost.copy(
                    lifecycleState = PostLifecycleState.BIN,
                    deletedAt = System.currentTimeMillis()
                )
            }
        }
    }

    override fun restorePost(postId: String) {
        val index = posts.indexOfFirst { post ->
            post.id == postId
        }

        if (index >= 0) {
            val existingPost = posts[index]

            if (existingPost.lifecycleState == PostLifecycleState.BIN) {
                posts[index] = existingPost.copy(
                    lifecycleState = PostLifecycleState.ACTIVE,
                    deletedAt = null
                )
            }
        }
    }

    override fun movePostToAsh(postId: String) {
        val index = posts.indexOfFirst { post ->
            post.id == postId
        }

        if (index >= 0) {
            val existingPost = posts[index]

            if (existingPost.lifecycleState != PostLifecycleState.ASH) {
                posts[index] = existingPost.copy(
                    lifecycleState = PostLifecycleState.ASH,
                    deletedAt = System.currentTimeMillis()
                )
            }
        }
    }
}
