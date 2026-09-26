package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Reaction
import org.junit.Assert.assertEquals
import org.junit.Test

class InMemoryReactionLocalDataSourceTest {

    @Test
    fun getReactions_returnsOnlyReactionsForRequestedPost() {
        val dataSource = InMemoryReactionLocalDataSource()

        val firstReaction = Reaction(
            id = "reaction-1",
            postId = "post-1",
            userId = "user-1"
        )

        val secondReaction = Reaction(
            id = "reaction-2",
            postId = "post-2",
            userId = "user-2"
        )

        dataSource.saveReaction(firstReaction)
        dataSource.saveReaction(secondReaction)

        val result = dataSource.getReactions("post-1")

        assertEquals(listOf(firstReaction), result)
    }

    @Test
    fun saveReaction_storesReaction() {
        val dataSource = InMemoryReactionLocalDataSource()

        val reaction = Reaction(
            id = "reaction-1",
            postId = "post-1",
            userId = "user-1"
        )

        dataSource.saveReaction(reaction)

        assertEquals(
            listOf(reaction),
            dataSource.getReactions("post-1")
        )
    }

    @Test
    fun getReactions_whenPostHasNoReactions_returnsEmptyList() {
        val dataSource = InMemoryReactionLocalDataSource()

        val result = dataSource.getReactions("post-1")

        assertEquals(emptyList<Reaction>(), result)
    }
}
