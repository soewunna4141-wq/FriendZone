package com.fz.friendzone.core.media

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fz.friendzone.core.saved.SavedPostDao
import com.fz.friendzone.core.saved.SavedPostEntity
import com.fz.friendzone.data.local.PostDao
import com.fz.friendzone.data.local.PostEntity

@Database(
entities = [
MediaAssetEntity::class,
SavedPostEntity::class,
PostEntity::class
],
version = 3,
exportSchema = false
)
abstract class FriendZoneDatabase : RoomDatabase() {

abstract fun mediaAssetDao(): MediaAssetDao

abstract fun savedPostDao(): SavedPostDao

abstract fun postDao(): PostDao

companion object {

    val MIGRATION_2_3 = object : Migration(2, 3) {

        override fun migrate(
            database: SupportSQLiteDatabase
        ) {
            database.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `news_posts` (
                    `id` TEXT NOT NULL,
                    `userId` TEXT NOT NULL,
                    `caption` TEXT NOT NULL,
                    `mediaAssetId` TEXT,
                    `mediaUrl` TEXT,
                    `mediaType` TEXT NOT NULL,
                    `audience` TEXT NOT NULL,
                    `createdAt` INTEGER NOT NULL,
                    `lifecycleState` TEXT NOT NULL,
                    `deletedAt` INTEGER,
                    PRIMARY KEY(`id`)
                )
                """.trimIndent()
            )
        }
    }
}

}
