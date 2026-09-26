package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Reaction

class InMemoryReactionLocalDataSource : ReactionLocalDataSource {

    private val reactions = mutableListOf<Reaction>()

    override fun getReactions(postId: String): List<Reaction> {
        return reactions.filter { reaction ->
            reaction.postId == postId
        }
    }

    override fun saveReaction(reaction: Reaction) {
        reactions.add(reaction)
    }
}
