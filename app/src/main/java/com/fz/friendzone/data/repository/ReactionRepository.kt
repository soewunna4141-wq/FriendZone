package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Reaction

interface ReactionRepository {

    fun getReactions(postId: String): List<Reaction>

    fun saveReaction(reaction: Reaction)
}
