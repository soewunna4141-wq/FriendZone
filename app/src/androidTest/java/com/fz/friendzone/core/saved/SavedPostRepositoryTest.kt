
package com.fz.friendzone.core.saved

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fz.friendzone.core.media.FriendZoneDatabase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class SavedPostRepositoryTest {

    private lateinit var context: Context
    private lateinit var databaseName: String
    private lateinit var database: FriendZoneDatabase
    private lateinit var repository: SavedPostRepository

    @Before
    fun setUp() {
        context = InstrumentationRegistry
            .getInstrumentation()
            .targetContext

        databaseName = "saved-post-test-${UUID.randomUUID()}"
        openDatabase()
    }

    private fun openDatabase() {
        database = Room.databaseBuilder(
            context,
            FriendZoneDatabase::class.java,
            databaseName
        )
            .allowMainThreadQueries()
            .build()

        repository = RoomSavedPostRepository(
            database.savedPostDao()
        )
    }

    @After
    fun tearDown() {
        database.close()
        context.deleteDatabase(databaseName)
    }

    @Test
    fun savedPost_survivesDatabaseReopen() {
        assertTrue(
            repository.save("user-1", "post-1")
        )

        database.close()
        openDatabase()

        assertTrue(
            repository.isSaved("user-1", "post-1")
        )

        assertEquals(
            listOf("post-1"),
            repository.getSavedPosts("user-1")
                .map { it.postId }
        )
    }

    @Test
    fun savingSamePostTwice_doesNotCreateDuplicate() {
        assertTrue(
            repository.save("user-1", "post-1")
        )

        assertFalse(
            repository.save("user-1", "post-1")
        )

        assertEquals(
            1,
            repository.getSavedPosts("user-1").size
        )

        assertTrue(
            repository.isSaved("user-1", "post-1")
        )
    }

    @Test
    fun savedPosts_areScopedToEachUser() {
        assertTrue(
            repository.save("user-1", "post-1")
        )

        assertTrue(
            repository.save("user-2", "post-1")
        )

        assertEquals(
            listOf("post-1"),
            repository.getSavedPosts("user-1")
                .map { it.postId }
        )

        assertEquals(
            listOf("post-1"),
            repository.getSavedPosts("user-2")
                .map { it.postId }
        )

        assertTrue(
            repository.isSaved("user-1", "post-1")
        )

        assertTrue(
            repository.isSaved("user-2", "post-1")
        )
    }

    @Test
    fun removingSavedPost_removesOnlyThatUsersRecord() {
        assertTrue(
            repository.save("user-1", "post-1")
        )

        assertTrue(
            repository.save("user-2", "post-1")
        )

        assertTrue(
            repository.remove("user-1", "post-1")
        )

        assertFalse(
            repository.isSaved("user-1", "post-1")
        )

        assertTrue(
            repository.isSaved("user-2", "post-1")
        )

        assertFalse(
            repository.remove("user-1", "post-1")
        )
    }

    @Test
    fun blankIds_areRejected() {
        assertFalse(
            repository.save("", "post-1")
        )

        assertFalse(
            repository.save("user-1", "")
        )

        assertTrue(
            repository.getSavedPosts("").isEmpty()
        )

        assertFalse(
            repository.isSaved("user-1", "")
        )

        assertFalse(
            repository.remove("user-1", "")
        )
    }
}
