package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Reel

interface ReelsRepository {

    fun getReels(): List<Reel>

    fun saveReel(reel: Reel)
}
