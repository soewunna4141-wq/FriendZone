package com.fz.friendzone.core.model

data class Friend(
    val id: String,
    val userId: String,
    val friendUserId: String,
    val createdAt: Long = System.currentTimeMillis()
)
