package com.fz.friendzone.core.model

data class Post(
    val id: String,
    val userId: String,
    val caption: String,
    val mediaUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
