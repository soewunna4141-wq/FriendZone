package com.fz.friendzone.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.PostAudience
import com.fz.friendzone.core.model.PostLifecycleState
import com.fz.friendzone.core.model.PostMediaType

@Entity(tableName = "news_posts")
data class PostEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val caption: String,
    val mediaAssetId: String?,
    val mediaUrl: String?,
    val mediaType: String,
    val audience: String,
    val createdAt: Long,
    val lifecycleState: String,
    val deletedAt: Long?
) {
    fun toPost(): Post {
        return Post(
            id = id,
            userId = userId,
            caption = caption,
            mediaAssetId = mediaAssetId,
            mediaUrl = mediaUrl,
            mediaType = PostMediaType.valueOf(mediaType),
            audience = PostAudience.valueOf(audience),
            createdAt = createdAt,
            lifecycleState = PostLifecycleState.valueOf(lifecycleState),
            deletedAt = deletedAt
        )
    }

    companion object {
        fun fromPost(post: Post): PostEntity {
            return PostEntity(
                id = post.id,
                userId = post.userId,
                caption = post.caption,
                mediaAssetId = post.mediaAssetId,
                mediaUrl = post.mediaUrl,
                mediaType = post.mediaType.name,
                audience = post.audience.name,
                createdAt = post.createdAt,
                lifecycleState = post.lifecycleState.name,
                deletedAt = post.deletedAt
            )
        }
    }
}
