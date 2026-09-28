package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Reaction
import com.fz.friendzone.data.local.ReactionLocalDataSource
import org.junit.Assert.assertEquals
import org.junit.Test

class ReactionRepositoryImplTest {

    @Test
    fun getReactions_delegatesToLocalDataSource() {
        val reaction = Reaction(
            id = "reaction-1",
            postId = "post-1",
            userId = "user-1"
        )

        val localDataSource = FakeReactionLocalDataSource(
            reactions = listOf(reaction)
        )
        val repository = ReactionRepositoryImpl(localDataSource)

        val result = repository.getReactions("post-1")

        assertEquals(listOf(reaction), result)
    }

    @Test
    fun saveReaction_delegatesToLocalDataSource() {
        val localDataSource = FakeReactionLocalDataSource()
        val repository = ReactionRepositoryImpl(localDataSource)

        val reaction = Reaction(
            id = "reaction-1",
            postId = "post-1",
            userId = "user-1"
        )

        repository.saveReaction(reaction)

        assertEquals(listOf(reaction), localDataSource.savedReactions)
    }

    @Test
    fun saveReaction_allowsReactionFromDifferentUser() {
        val existingReaction = Reaction(
            id = "reaction-1",
            postId = "post-1",
            userId = "user-1",
            type = "LIKE"
        )

        val newReaction = Reaction(
            id = "reaction-2",
            postId = "post-1",
            userId = "user-2",
            type = "LIKE"
        )

        val localDataSource = FakeReactionLocalDataSource(
            reactions = listOf(existingReaction)
        )
        val repository = ReactionRepositoryImpl(localDataSource)

        repository.saveReaction(newReaction)

        assertEquals(listOf(newReaction), localDataSource.savedReactions)
    }

    @Test
    fun saveReaction_doesNotSaveDuplicateReactionFromSameUser() {
        val existingReaction = Reaction(
            id = "reaction-1",
            postId = "post-1",
            userId = "user-1",
            type = "LIKE"
        )

        val duplicateReaction = Reaction(
            id = "reaction-2",
            postId = "post-1",
            userId = "user-1",
            type = "LIKE"
        )

        val localDataSource = FakeReactionLocalDataSource(
            reactions = listOf(existingReaction)
        )
        val repository = ReactionRepositoryImpl(localDataSource)

        repository.saveReaction(duplicateReaction)

        assertEquals(emptyList<Reaction>(), localDataSource.savedReactions)
    }

    private class FakeReactionLocalDataSource(
        private val reactions: List<Reaction> = emptyList()
    ) : ReactionLocalDataSource {

        val savedReactions = mutableListOf<Reaction>()

        override fun getReactions(postId: String): List<Reaction> {
            return reactions.filter { it.postId == postId }
        }

        override fun saveReaction(reaction: Reaction) {
            savedReactions.add(reaction)
        }
    }
}
