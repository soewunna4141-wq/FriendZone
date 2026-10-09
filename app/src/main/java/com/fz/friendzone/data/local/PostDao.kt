package com.fz.friendzone.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PostDao {

    @Query(
        """
        SELECT * FROM news_posts
        WHERE lifecycleState = 'ACTIVE'
        ORDER BY createdAt DESC
        """
    )
    fun getActivePosts(): List<PostEntity>

    @Query(
        """
        SELECT * FROM news_posts
        WHERE lifecycleState = 'BIN'
        ORDER BY COALESCE(deletedAt, createdAt) DESC
        """
    )
    fun getBinPosts(): List<PostEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(post: PostEntity)

    @Update
    fun update(post: PostEntity)

    @Query(
        """
        UPDATE news_posts
        SET lifecycleState = 'BIN',
            deletedAt = :deletedAt
        WHERE id = :postId
          AND lifecycleState = 'ACTIVE'
        """
    )
    fun moveToBin(
        postId: String,
        deletedAt: Long
    )

    @Query(
        """
        UPDATE news_posts
        SET lifecycleState = 'ACTIVE',
            deletedAt = NULL
        WHERE id = :postId
          AND lifecycleState = 'BIN'
        """
    )
    fun restoreFromBin(postId: String)

    @Query(
        """
        UPDATE news_posts
        SET lifecycleState = 'ASH',
            deletedAt = :deletedAt
        WHERE id = :postId
          AND lifecycleState != 'ASH'
        """
    )
    fun moveToAsh(
        postId: String,
        deletedAt: Long
    )
}
