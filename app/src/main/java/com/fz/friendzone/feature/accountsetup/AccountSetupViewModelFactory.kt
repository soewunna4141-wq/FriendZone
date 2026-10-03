package com.fz.friendzone.feature.accountsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AccountSetupViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(AccountSetupViewModel::class.java)) {
            return AccountSetupViewModel() as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
