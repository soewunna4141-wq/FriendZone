package com.fz.friendzone.core.saved

import androidx.room.Entity
import com.fz.friendzone.core.model.SavedPost

@Entity(
    tableName = "saved_posts",
    primaryKeys = ["userId", "postId"]
)
data class SavedPostEntity(
    val userId: String,
    val postId: String,
    val savedAt: Long
) {
    fun toDomain(): SavedPost {
        return SavedPost(
            userId = userId,
            postId = postId,
            savedAt = savedAt
        )
    }

    companion object {
        fun fromDomain(savedPost: SavedPost): SavedPostEntity {
            return SavedPostEntity(
                userId = savedPost.userId,
                postId = savedPost.postId,
                savedAt = savedPost.savedAt
            )
        }
    }
}
