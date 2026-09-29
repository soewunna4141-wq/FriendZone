package com.fz.friendzone.feature.reels

sealed interface ReelsAction {

    data object Load : ReelsAction

    data class SaveReel(
        val reel: com.fz.friendzone.core.model.Reel
    ) : ReelsAction
}
