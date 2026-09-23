package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Post

interface NewsRepository {

    fun getPosts(): List<Post>
}
