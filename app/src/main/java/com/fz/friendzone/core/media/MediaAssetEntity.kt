package com.fz.friendzone.core.media

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_assets")
data class MediaAssetEntity(
    @PrimaryKey
    val id: String,
    val ownerId: String,
    val uri: String,
    val type: String,
    val source: String,
    val createdAt: Long
)
