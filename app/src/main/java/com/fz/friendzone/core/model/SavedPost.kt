package com.fz.friendzone.core.model

data class SavedPost(
    val userId: String,
    val postId: String,
    val savedAt: Long = System.currentTimeMillis()
)
