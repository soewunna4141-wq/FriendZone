package com.fz.friendzone.feature.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import com.fz.friendzone.R
import com.fz.friendzone.app.FriendZoneDependencies
import com.fz.friendzone.core.model.Account

@Composable
fun AccountScreen(
    dependencies: FriendZoneDependencies
) {
    val viewModel: AccountViewModel = viewModel(
        factory = dependencies.accountViewModelFactory
    )

    val uiState by viewModel.uiState.collectAsState()

    val account = uiState.account

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (account == null) {
            Text(
                text = stringResource(R.string.account_no_account)
            )
        } else {
            Text(
                text = buildString {
                    append(
                        stringResource(
                            R.string.account_id,
                            account.id
                        )
                    )
                    append("\n")
                    append(
                        stringResource(
                            R.string.account_active,
                            account.isActive
                        )
                    )
                }
            )

            Button(
                onClick = {
                    viewModel.onAction(
                        AccountAction.Save(
                            Account(
                                id = account.id,
                                isActive = !account.isActive
                            )
                        )
                    )
                }
            ) {
                Text(
                    text = stringResource(
                        R.string.account_toggle_active
                    )
                )
            }
        }
    }
}
