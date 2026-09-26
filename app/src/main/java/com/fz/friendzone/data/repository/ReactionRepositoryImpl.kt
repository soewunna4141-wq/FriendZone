package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Reaction
import com.fz.friendzone.data.local.ReactionLocalDataSource

class ReactionRepositoryImpl(
    private val localDataSource: ReactionLocalDataSource
) : ReactionRepository {

    override fun getReactions(postId: String): List<Reaction> {
        return localDataSource.getReactions(postId)
    }

    override fun saveReaction(reaction: Reaction) {
        localDataSource.saveReaction(reaction)
    }
}
