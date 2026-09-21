package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Account

interface AccountRepository {

    fun getAccount(): Account?

    fun saveAccount(account: Account)
}
