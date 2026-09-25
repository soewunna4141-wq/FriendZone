package com.fz.friendzone.feature.news

import com.fz.friendzone.core.model.Post

sealed interface NewsAction {

    data object Load : NewsAction

    data class CreatePost(
        val post: Post
    ) : NewsAction
}
