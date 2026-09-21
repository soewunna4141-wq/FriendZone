package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Account

class InMemoryAccountLocalDataSource : AccountLocalDataSource {

    private var account: Account? = Account(
        id = "demo-account",
        isActive = true
    )

    override fun getAccount(): Account? {
        return account
    }

    override fun saveAccount(account: Account) {
        this.account = account
    }
}
