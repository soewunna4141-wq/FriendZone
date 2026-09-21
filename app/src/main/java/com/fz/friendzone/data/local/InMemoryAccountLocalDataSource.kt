package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Account

class InMemoryAccountLocalDataSource : AccountLocalDataSource {

    private var account: Account? = null

    override fun getAccount(): Account? {
        return account
    }
}
