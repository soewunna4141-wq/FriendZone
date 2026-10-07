package com.fz.friendzone.core.model

enum class PostMediaType {
    TEXT,
    IMAGE,
    VIDEO
}

enum class PostLifecycleState {
    ACTIVE,
    BIN,
    ASH
}

enum class PostAudience {
    PUBLIC,
    FOLLOWERS,
    FRIENDS_OF_FRIENDS,
    FRIENDS,
    PRIVATE
}

data class Post(
    val id: String,
    val userId: String,
    val caption: String,
    val mediaAssetId: String? = null,
    @Deprecated("Use mediaAssetId to reference media through MediaLibrary.")
    val mediaUrl: String? = null,
    val mediaType: PostMediaType = PostMediaType.TEXT,
    val audience: PostAudience = PostAudience.PUBLIC,
    val createdAt: Long = System.currentTimeMillis(),
    val lifecycleState: PostLifecycleState = PostLifecycleState.ACTIVE,
    val deletedAt: Long? = null
)
