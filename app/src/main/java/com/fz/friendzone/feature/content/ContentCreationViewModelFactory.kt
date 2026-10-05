package com.fz.friendzone.feature.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.ProfileRepository
import com.fz.friendzone.data.repository.ReelsRepository

class ContentCreationViewModelFactory(
    private val newsRepository: NewsRepository,
    private val reelsRepository: ReelsRepository,
    private val profileRepository: ProfileRepository,
    private val target: ContentCreationTarget
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContentCreationViewModel::class.java)) {
            return ContentCreationViewModel(
                newsRepository = newsRepository,
                reelsRepository = reelsRepository,
                profileRepository = profileRepository,
                target = target
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
