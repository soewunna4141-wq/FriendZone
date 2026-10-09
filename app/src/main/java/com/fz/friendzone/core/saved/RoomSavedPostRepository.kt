package com.fz.friendzone.core.saved

import com.fz.friendzone.core.model.SavedPost

class RoomSavedPostRepository(
    private val dao: SavedPostDao
) : SavedPostRepository {

    override fun save(
        userId: String,
        postId: String
    ): Boolean {
        if (userId.isBlank() || postId.isBlank()) {
            return false
        }

        return dao.save(
            SavedPostEntity(
                userId = userId,
                postId = postId,
                savedAt = System.currentTimeMillis()
            )
        ) != -1L
    }

    override fun getSavedPosts(
        userId: String
    ): List<SavedPost> {
        if (userId.isBlank()) {
            return emptyList()
        }

        return dao.getByUser(userId).map {
            it.toDomain()
        }
    }

    override fun isSaved(
        userId: String,
        postId: String
    ): Boolean {
        if (userId.isBlank() || postId.isBlank()) {
            return false
        }

        return dao.isSaved(
            userId = userId,
            postId = postId
        )
    }

    override fun remove(
        userId: String,
        postId: String
    ): Boolean {
        if (userId.isBlank() || postId.isBlank()) {
            return false
        }

        return dao.remove(
            userId = userId,
            postId = postId
        ) > 0
    }
}
