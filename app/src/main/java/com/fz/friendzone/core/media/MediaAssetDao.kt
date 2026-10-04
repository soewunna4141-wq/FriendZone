package com.fz.friendzone.core.media

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MediaAssetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(asset: MediaAssetEntity)

    @Query("SELECT * FROM media_assets WHERE id = :id LIMIT 1")
    fun getById(id: String): MediaAssetEntity?

    @Query("SELECT * FROM media_assets WHERE ownerId = :ownerId ORDER BY createdAt DESC")
    fun getByOwner(ownerId: String): List<MediaAssetEntity>

    @Query("SELECT * FROM media_assets ORDER BY createdAt DESC")
    fun getAll(): List<MediaAssetEntity>

    @Query("DELETE FROM media_assets WHERE id = :id")
    fun delete(id: String)
}
