package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Reaction

interface ReactionLocalDataSource {

    fun getReactions(postId: String): List<Reaction>

    fun saveReaction(reaction: Reaction)
}
