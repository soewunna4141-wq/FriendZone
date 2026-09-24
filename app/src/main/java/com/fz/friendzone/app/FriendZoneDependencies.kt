package com.fz.friendzone.app

import com.fz.friendzone.data.repository.AccountRepository
import com.fz.friendzone.data.repository.AccountRepositoryFactory
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.NewsRepositoryFactory
import com.fz.friendzone.data.repository.ProfileRepository
import com.fz.friendzone.data.repository.ProfileRepositoryFactory
import com.fz.friendzone.feature.account.AccountViewModelFactory
import com.fz.friendzone.feature.profile.ProfileViewModelFactory

class FriendZoneDependencies {

    val accountRepository: AccountRepository by lazy {
        AccountRepositoryFactory.create()
    }

    val accountViewModelFactory: AccountViewModelFactory by lazy {
        AccountViewModelFactory(accountRepository)
    }

    val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryFactory.create()
    }

    val profileViewModelFactory: ProfileViewModelFactory by lazy {
        ProfileViewModelFactory(profileRepository)
    }

    val newsRepository: NewsRepository by lazy {
        NewsRepositoryFactory.create()
    }
}
