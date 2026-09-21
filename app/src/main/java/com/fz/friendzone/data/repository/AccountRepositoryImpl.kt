package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Account
import com.fz.friendzone.data.local.AccountLocalDataSource

class AccountRepositoryImpl(
    private val localDataSource: AccountLocalDataSource
) : AccountRepository {

    override fun getAccount(): Account? {
        return localDataSource.getAccount()
    }

    override fun saveAccount(account: Account) {
        localDataSource.saveAccount(account)
    }
}
