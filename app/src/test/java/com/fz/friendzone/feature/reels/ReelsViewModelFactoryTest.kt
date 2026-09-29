package com.fz.friendzone.feature.reels

import com.fz.friendzone.data.local.InMemoryReelsLocalDataSource
import com.fz.friendzone.data.repository.ReelsRepositoryImpl
import org.junit.Assert.assertNotNull
import org.junit.Test

class ReelsViewModelFactoryTest {

    @Test
    fun create_reelsViewModel_returnsViewModel() {
        val repository = ReelsRepositoryImpl(
            InMemoryReelsLocalDataSource()
        )
        val factory = ReelsViewModelFactory(repository)

        val viewModel = factory.create(ReelsViewModel::class.java)

        assertNotNull(viewModel)
    }
}
