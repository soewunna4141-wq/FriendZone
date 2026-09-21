package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Account

interface AccountLocalDataSource {

    fun getAccount(): Account?

    fun saveAccount(account: Account)
}
