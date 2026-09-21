package com.fz.friendzone.feature.account

import androidx.lifecycle.ViewModel
import com.fz.friendzone.core.model.Account
import com.fz.friendzone.data.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AccountUiState(
    val account: Account? = null
)

class AccountViewModel(
    private val repository: AccountRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AccountUiState(
            account = repository.getAccount()
        )
    )

    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    fun saveAccount(account: Account) {
        repository.saveAccount(account)

        val savedAccount = repository.getAccount()

        _uiState.value = AccountUiState(
            account = savedAccount
        )
    }
}
