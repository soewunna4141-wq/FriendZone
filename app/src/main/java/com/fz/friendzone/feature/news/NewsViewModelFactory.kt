package com.fz.friendzone.feature.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.ProfileRepository

class NewsViewModelFactory(
    private val newsRepository: NewsRepository,
    private val profileRepository: ProfileRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        return NewsViewModel(
            newsRepository = newsRepository,
            profileRepository = profileRepository
        ) as T
    }
}
