package com.fz.friendzone.feature.news

import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.Reaction

sealed interface NewsAction {

    data object Load : NewsAction

    data class CreatePost(
        val post: Post
    ) : NewsAction

    data class SaveReaction(
        val reaction: Reaction
    ) : NewsAction
}
