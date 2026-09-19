package com.fz.friendzone.core.model

data class Comment(
    val id: String,
    val postId: String,
    val userId: String,
    val text: String,
    val createdAt: Long = System.currentTimeMillis()
)
