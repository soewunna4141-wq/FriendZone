package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Reel
import com.fz.friendzone.data.local.ReelsLocalDataSource

class ReelsRepositoryImpl(
    private val localDataSource: ReelsLocalDataSource
) : ReelsRepository {

    override fun getReels(): List<Reel> {
        return localDataSource.getReels()
    }

    override fun saveReel(reel: Reel) {
        localDataSource.saveReel(reel)
    }
}
