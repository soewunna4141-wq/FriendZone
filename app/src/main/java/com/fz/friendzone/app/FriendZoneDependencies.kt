package com.fz.friendzone.app

import com.fz.friendzone.data.repository.AccountRepository
import com.fz.friendzone.data.repository.AccountRepositoryFactory
import com.fz.friendzone.data.repository.CommentRepository
import com.fz.friendzone.data.repository.CommentRepositoryFactory
import com.fz.friendzone.data.repository.FriendRepository
import com.fz.friendzone.data.repository.FriendRepositoryFactory
import com.fz.friendzone.data.repository.FollowRepository
import com.fz.friendzone.data.repository.FollowRepositoryFactory
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.NewsRepositoryFactory
import com.fz.friendzone.data.repository.ProfileRepository
import com.fz.friendzone.data.repository.ProfileRepositoryFactory
import com.fz.friendzone.data.repository.ReactionRepository
import com.fz.friendzone.data.repository.ReactionRepositoryFactory
import com.fz.friendzone.data.repository.UserRepository
import com.fz.friendzone.data.repository.UserRepositoryFactory
import com.fz.friendzone.feature.account.AccountViewModelFactory
import com.fz.friendzone.feature.follow.FollowViewModelFactory
import com.fz.friendzone.feature.friend.FriendViewModelFactory
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

    val friendRepository: FriendRepository by lazy {
        FriendRepositoryFactory.create()
    }

    val friendViewModelFactory: FriendViewModelFactory by lazy {
        FriendViewModelFactory(friendRepository)
    }

    val followRepository: FollowRepository by lazy {
        FollowRepositoryFactory.create()
    }

    val followViewModelFactory: FollowViewModelFactory by lazy {
        FollowViewModelFactory(followRepository)
    }
}
