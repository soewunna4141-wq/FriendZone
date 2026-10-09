package com.fz.friendzone.core.media

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fz.friendzone.data.local.PostEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class NewsPostDatabaseMigrationTest {

private lateinit var context: Context
private lateinit var databaseName: String
private var database: FriendZoneDatabase? = null

@Before
fun setUp() {
    context = InstrumentationRegistry
        .getInstrumentation()
        .targetContext

    databaseName = "news-post-migration-${UUID.randomUUID()}"
}

@After
fun tearDown() {
    database?.close()
    context.deleteDatabase(databaseName)
}

@Test
fun migrationToVersion3_preservesExistingDataAndPersistsNewsPosts() {
    createVersion2Database()

    database = openVersion3Database()
    val firstOpenDatabase = requireNotNull(database)

    assertEquals(
        "media-1",
        firstOpenDatabase.mediaAssetDao()
            .getById("media-1")
            ?.id
    )

    assertTrue(
        firstOpenDatabase.savedPostDao()
            .isSaved("user-1", "saved-post-1")
    )

    val post = PostEntity(
        id = "news-post-1",
        userId = "user-1",
        caption = "Room persistence test",
        mediaAssetId = "media-1",
        mediaUrl = null,
        mediaType = "IMAGE",
        audience = "FRIENDS",
        createdAt = 123456789L,
        lifecycleState = "ACTIVE",
        deletedAt = null
    )

    firstOpenDatabase.postDao().insertOrReplace(post)

    firstOpenDatabase.close()
    database = null

    database = openVersion3Database()
    val reopenedDatabase = requireNotNull(database)

    val persistedPost = reopenedDatabase.postDao()
        .getActivePosts()
        .single { it.id == "news-post-1" }

    assertEquals(post.id, persistedPost.id)
    assertEquals(post.userId, persistedPost.userId)
    assertEquals(post.caption, persistedPost.caption)
    assertEquals(post.mediaAssetId, persistedPost.mediaAssetId)
    assertEquals(post.mediaUrl, persistedPost.mediaUrl)
    assertEquals(post.mediaType, persistedPost.mediaType)
    assertEquals(post.audience, persistedPost.audience)
    assertEquals(post.createdAt, persistedPost.createdAt)
    assertEquals(post.lifecycleState, persistedPost.lifecycleState)
    assertEquals(post.deletedAt, persistedPost.deletedAt)

    assertNotNull(
        reopenedDatabase.mediaAssetDao()
            .getById("media-1")
    )

    assertTrue(
        reopenedDatabase.savedPostDao()
            .isSaved("user-1", "saved-post-1")
    )
}

private fun createVersion2Database() {
    val legacyDatabase = context.openOrCreateDatabase(
        databaseName,
        Context.MODE_PRIVATE,
        null
    )

    try {
        legacyDatabase.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `media_assets` (
                `id` TEXT NOT NULL,
                `ownerId` TEXT NOT NULL,
                `uri` TEXT NOT NULL,
                `type` TEXT NOT NULL,
                `source` TEXT NOT NULL,
                `createdAt` INTEGER NOT NULL,
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
        )

        legacyDatabase.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `saved_posts` (
                `userId` TEXT NOT NULL,
                `postId` TEXT NOT NULL,
                `savedAt` INTEGER NOT NULL,
                PRIMARY KEY(`userId`, `postId`)
            )
            """.trimIndent()
        )

        legacyDatabase.execSQL(
            """
            INSERT INTO `media_assets`
                (`id`, `ownerId`, `uri`, `type`, `source`, `createdAt`)
            VALUES (
                'media-1',
                'user-1',
                'content://friendzone/media-1',
                'IMAGE',
                'GALLERY',
                1000
            )
            """.trimIndent()
        )

        legacyDatabase.execSQL(
            """
            INSERT INTO `saved_posts`
                (`userId`, `postId`, `savedAt`)
            VALUES ('user-1', 'saved-post-1', 2000)
            """.trimIndent()
        )

        legacyDatabase.version = 2
    } finally {
        legacyDatabase.close()
    }
}

private fun openVersion3Database(): FriendZoneDatabase {
    return Room.databaseBuilder(
        context,
        FriendZoneDatabase::class.java,
        databaseName
    )
        .addMigrations(
            FriendZoneDatabase.MIGRATION_2_3
        )
        .allowMainThreadQueries()
        .build()
}

}
