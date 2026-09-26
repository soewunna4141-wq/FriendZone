package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Reaction
import org.junit.Assert.assertEquals
import org.junit.Test

class ReactionRepositoryFactoryTest {

    @Test
    fun create_returnsRepositoryThatCanSaveAndReadReactions() {
        val repository = ReactionRepositoryFactory.create()

        val reaction = Reaction(
            id = "reaction-1",
            postId = "post-1",
            userId = "user-1"
        )

        repository.saveReaction(reaction)

        assertEquals(
            listOf(reaction),
            repository.getReactions("post-1")
        )
    }
}
