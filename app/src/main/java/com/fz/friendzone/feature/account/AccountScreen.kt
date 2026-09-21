package com.fz.friendzone.feature.account

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fz.friendzone.app.FriendZoneDependencies
import com.fz.friendzone.core.model.Account

@Composable
fun AccountScreen() {

    val viewModel: AccountViewModel = viewModel(
        factory = FriendZoneDependencies.accountViewModelFactory
    )

    val uiState by viewModel.uiState.collectAsState()

    val account = uiState.account

    if (account == null) {
        Text(text = "No account")
    } else {
        Text(
            text = "Account ID: ${account.id}\nActive: ${account.isActive}"
        )

        Button(
            onClick = {
                viewModel.saveAccount(
                    Account(
                        id = account.id,
                        isActive = !account.isActive
                    )
                )
            }
        ) {
            Text(text = "Toggle Active")
        }
    }
}
