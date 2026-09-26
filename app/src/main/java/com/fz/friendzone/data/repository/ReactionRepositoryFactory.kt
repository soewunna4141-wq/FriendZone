package com.fz.friendzone.data.repository

import com.fz.friendzone.data.local.InMemoryReactionLocalDataSource

object ReactionRepositoryFactory {

    fun create(): ReactionRepository {
        return ReactionRepositoryImpl(
            localDataSource = InMemoryReactionLocalDataSource()
        )
    }
}
