package com.fz.friendzone.core.saved

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedPostDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(savedPost: SavedPostEntity): Long

    @Query(
        """
        SELECT * FROM saved_posts
        WHERE userId = :userId
        ORDER BY savedAt DESC
        """
    )
    fun getByUser(userId: String): List<SavedPostEntity>

    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM saved_posts
            WHERE userId = :userId
            AND postId = :postId
        )
        """
    )
    fun isSaved(
        userId: String,
        postId: String
    ): Boolean

    @Query(
        """
        DELETE FROM saved_posts
        WHERE userId = :userId
        AND postId = :postId
        """
    )
    fun remove(
        userId: String,
        postId: String
    ): Int
}
