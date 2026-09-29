package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Reel

class InMemoryReelsLocalDataSource : ReelsLocalDataSource {

    private val reels = mutableListOf<Reel>()

    override fun getReels(): List<Reel> {
        return reels
            .sortedByDescending { reel ->
                reel.createdAt
            }
    }

    override fun saveReel(reel: Reel) {
        reels.add(reel)
    }
}
