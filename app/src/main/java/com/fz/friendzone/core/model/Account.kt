package com.fz.friendzone.core.model

/**
 * Account represents the system-level identity of a FriendZone user.
 *
 * Account is intentionally kept separate from User and Profile.
 */
data class Account(
    val id: String,
    val isActive: Boolean = true
)
