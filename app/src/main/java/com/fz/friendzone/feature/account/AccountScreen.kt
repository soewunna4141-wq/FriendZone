package com.fz.friendzone.feature.account

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fz.friendzone.data.repository.AccountRepositoryFactory

@Composable
fun AccountScreen() {

    val repository = AccountRepositoryFactory.create()

    val viewModel: AccountViewModel = viewModel(
        factory = AccountViewModelFactory(repository)
    )

    val uiState by viewModel.uiState.collectAsState()

    val account = uiState.account

    if (account == null) {
        Text(text = "No account")
    } else {
        Text(
            text = "Account ID: ${account.id}\nActive: ${account.isActive}"
        )
    }
}
