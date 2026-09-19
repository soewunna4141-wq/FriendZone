package com.fz.friendzone.core.model

data class User(
    val id: String,
    val name: String,
    val username: String,
    val profileImageUrl: String? = null
)
