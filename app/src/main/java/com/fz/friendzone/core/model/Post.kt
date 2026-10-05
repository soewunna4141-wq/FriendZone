package com.fz.friendzone.core.model

enum class PostMediaType {
    IMAGE,
    VIDEO
}

data class Post(
    val id: String,
    val userId: String,
    val caption: String,
    val mediaAssetId: String? = null,
    @Deprecated("Use mediaAssetId to reference media through MediaLibrary.")
    val mediaUrl: String? = null,
    val mediaType: PostMediaType = PostMediaType.IMAGE,
    val createdAt: Long = System.currentTimeMillis()
)
