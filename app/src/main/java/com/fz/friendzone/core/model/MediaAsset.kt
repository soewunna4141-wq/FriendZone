package com.fz.friendzone.core.model

enum class MediaType {
    IMAGE,
    VIDEO
}

enum class MediaSource {
    GALLERY,
    CAMERA,
    FRIENDZONE_LIBRARY
}

data class MediaAsset(
    val id: String,
    val ownerId: String,
    val uri: String,
    val type: MediaType,
    val source: MediaSource,
    val createdAt: Long = System.currentTimeMillis()
)
