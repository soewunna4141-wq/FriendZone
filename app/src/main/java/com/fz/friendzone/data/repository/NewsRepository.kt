package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Post

interface NewsRepository {

    fun getPosts(): List<Post>

    fun getBinPosts(): List<Post>

    fun savePost(post: Post)

    fun updatePost(post: Post)

    fun movePostToBin(postId: String)

    fun restorePost(postId: String)

    fun movePostToAsh(postId: String)
}
