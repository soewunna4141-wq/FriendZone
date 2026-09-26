package com.fz.friendzone.app

import com.fz.friendzone.data.repository.AccountRepository
import com.fz.friendzone.data.repository.AccountRepositoryFactory
import com.fz.friendzone.data.repository.CommentRepository
import com.fz.friendzone.data.repository.CommentRepositoryFactory
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.NewsRepositoryFactory
import com.fz.friendzone.data.repository.ProfileRepository
import com.fz.friendzone.data.repository.ProfileRepositoryFactory
import com.fz.friendzone.data.repository.ReactionRepository
import com.fz.friendzone.data.repository.ReactionRepositoryFactory
import com.fz.friendzone.data.repository.UserRepository
import com.fz.friendzone.data.repository.UserRepositoryFactory
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

    val reactionRepository: ReactionRepository by lazy {
        ReactionRepositoryFactory.create()
    }

    val commentRepository: CommentRepository by lazy {
        CommentRepositoryFactory.create()
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryFactory.create()
    }
}
