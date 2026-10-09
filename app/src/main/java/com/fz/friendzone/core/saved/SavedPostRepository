package com.fz.friendzone.core.saved

import com.fz.friendzone.core.model.SavedPost

interface SavedPostRepository {

    fun save(
        userId: String,
        postId: String
    ): Boolean

    fun getSavedPosts(
        userId: String
    ): List<SavedPost>

    fun isSaved(
        userId: String,
        postId: String
    ): Boolean

    fun remove(
        userId: String,
        postId: String
    ): Boolean
}
