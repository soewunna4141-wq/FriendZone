package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Reel
import com.fz.friendzone.data.local.InMemoryReelsLocalDataSource
import org.junit.Assert.assertEquals
import org.junit.Test

class ReelsRepositoryTest {

    @Test
    fun saveReel_thenGetReels_returnsSavedReel() {
        val localDataSource = InMemoryReelsLocalDataSource()
        val repository = ReelsRepositoryImpl(localDataSource)

        val reel = Reel(
            id = "reel-1",
            userId = "user-1",
            videoUrl = "video-1.mp4",
            caption = "Test reel"
        )

        repository.saveReel(reel)

        assertEquals(listOf(reel), repository.getReels())
    }
}
