package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Reel

interface ReelsLocalDataSource {

    fun getReels(): List<Reel>

    fun saveReel(reel: Reel)
}
