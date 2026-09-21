package com.fz.friendzone.feature.account

import androidx.lifecycle.ViewModel
import com.fz.friendzone.core.model.Account
import com.fz.friendzone.data.repository.AccountRepository

class AccountViewModel(
    private val repository: AccountRepository
) : ViewModel() {

    fun getAccount(): Account? {
        return repository.getAccount()
    }
}
