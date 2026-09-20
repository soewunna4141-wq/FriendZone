package com.fz.friendzone.core.model

/**
 * Profile represents the social identity of a FriendZone user.
 *
 * Profile is intentionally kept separate from Account and User.
 */
data class Profile(
    val userId: String,
    val displayName: String,
    val bio: String? = null,
    val profileImageUrl: String? = null
)
