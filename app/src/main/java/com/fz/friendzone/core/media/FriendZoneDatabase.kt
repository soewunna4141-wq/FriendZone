package com.fz.friendzone.core.media

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fz.friendzone.core.saved.SavedPostDao
import com.fz.friendzone.core.saved.SavedPostEntity

@Database(
    entities = [
        MediaAssetEntity::class,
        SavedPostEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class FriendZoneDatabase : RoomDatabase() {

    abstract fun mediaAssetDao(): MediaAssetDao

    abstract fun savedPostDao(): SavedPostDao
}
