package com.fz.friendzone.feature.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fz.friendzone.core.media.MediaLibrary
import com.fz.friendzone.data.repository.CommentRepository
import com.fz.friendzone.data.repository.FollowRepository
import com.fz.friendzone.data.repository.FriendRepository
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.ProfileRepository
import com.fz.friendzone.data.repository.ReactionRepository

class NewsViewModelFactory(
    private val newsRepository: NewsRepository,
    private val profileRepository: ProfileRepository,
    private val reactionRepository: ReactionRepository,
    private val commentRepository: CommentRepository,
    private val mediaLibrary: MediaLibrary,
    private val friendRepository: FriendRepository,
    private val followRepository: FollowRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        return NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository,
            reactionRepository = reactionRepository,
            commentRepository = commentRepository,
            mediaLibrary = mediaLibrary,
            friendRepository = friendRepository,
            followRepository = followRepository
        ) as T
    }
}
