package com.fz.friendzone.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fz.friendzone.data.repository.AccountRepositoryFactory

class AccountViewModelFactory : ViewModelProvider.Factory {

    private val repository = AccountRepositoryFactory.create()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            return AccountViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
