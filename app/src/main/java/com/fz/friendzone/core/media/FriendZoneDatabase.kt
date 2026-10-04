package com.fz.friendzone.core.media

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MediaAssetEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FriendZoneDatabase : RoomDatabase() {

    abstract fun mediaAssetDao(): MediaAssetDao
}
