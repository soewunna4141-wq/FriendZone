package com.fz.friendzone.core.model

data class Reaction(
    val id: String,
    val postId: String,
    val userId: String,
    val type: String = "LIKE",
    val createdAt: Long = System.currentTimeMillis()
)
