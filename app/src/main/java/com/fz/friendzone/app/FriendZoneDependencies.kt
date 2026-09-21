package com.fz.friendzone.app

import com.fz.friendzone.data.repository.AccountRepository
import com.fz.friendzone.data.repository.AccountRepositoryFactory
import com.fz.friendzone.feature.account.AccountViewModelFactory

object FriendZoneDependencies {

    val accountRepository: AccountRepository by lazy {
        AccountRepositoryFactory.create()
    }

    val accountViewModelFactory: AccountViewModelFactory by lazy {
        AccountViewModelFactory(accountRepository)
    }
}
