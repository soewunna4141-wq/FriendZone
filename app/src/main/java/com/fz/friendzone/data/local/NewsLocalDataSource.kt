package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Post

interface NewsLocalDataSource {

    fun getPosts(): List<Post>
}
