package com.fz.friendzone.feature.reels

import com.fz.friendzone.core.model.Reel
import com.fz.friendzone.data.local.InMemoryReelsLocalDataSource
import com.fz.friendzone.data.repository.ReelsRepositoryImpl
import org.junit.Assert.assertEquals
import org.junit.Test

class ReelsViewModelTest {

    @Test
    fun load_reels_updatesStateToSuccess() {
        val repository = ReelsRepositoryImpl(
            InMemoryReelsLocalDataSource()
        )
        val viewModel = ReelsViewModel(repository)

        viewModel.onAction(ReelsAction.Load)

        val state = viewModel.uiState.value

        assertEquals(
            ReelsUiState.Success(emptyList<Reel>()),
            state
        )
    }

    @Test
    fun saveReel_updatesStateWithSavedReel() {
        val repository = ReelsRepositoryImpl(
            InMemoryReelsLocalDataSource()
        )
        val viewModel = ReelsViewModel(repository)

        val reel = Reel(
            id = "reel-1",
            userId = "user-1",
            videoUrl = "video-1.mp4",
            caption = "Test reel"
        )

        viewModel.onAction(
            ReelsAction.SaveReel(reel)
        )

        val state = viewModel.uiState.value

        assertEquals(
            ReelsUiState.Success(listOf(reel)),
            state
        )
    }
}
