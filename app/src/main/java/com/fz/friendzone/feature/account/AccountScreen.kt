package com.fz.friendzone.feature.account

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fz.friendzone.data.repository.AccountRepositoryFactory

@Composable
fun AccountScreen() {

    val repository = AccountRepositoryFactory.create()

    val viewModel: AccountViewModel = viewModel(
        factory = AccountViewModelFactory(repository)
    )

    Text(text = "Account")
}
