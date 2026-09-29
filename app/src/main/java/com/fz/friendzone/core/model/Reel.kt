package com.fz.friendzone.core.model

data class Reel(
val id: String,
val userId: String,
val videoUrl: String,
val thumbnailUrl: String? = null,
val caption: String = "",
val createdAt: Long = System.currentTimeMillis()
)
