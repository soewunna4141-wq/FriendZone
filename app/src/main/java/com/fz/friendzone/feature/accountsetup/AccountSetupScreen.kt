package com.fz.friendzone.feature.accountsetup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AccountSetupScreen(
    viewModel: AccountSetupViewModel,
    onSetupComplete: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Account Setup"
        )

        when (uiState.step) {
            AccountSetupStep.Setup -> {
                TextField(
                    value = uiState.phoneNumber,
                    onValueChange = {
                        viewModel.onAction(
                            AccountSetupAction.PhoneNumberChanged(it)
                        )
                    },
                    label = {
                        Text(text = "Phone Number")
                    }
                )

                Button(
                    onClick = {
                        viewModel.onAction(
                            AccountSetupAction.RequestVerification
                        )
                    }
                ) {
                    Text(text = "Continue")
                }
            }

            AccountSetupStep.Verification -> {
                TextField(
                    value = uiState.verificationCode,
                    onValueChange = {
                        viewModel.onAction(
                            AccountSetupAction.VerificationCodeChanged(it)
                        )
                    },
                    label = {
                        Text(text = "Verification Code")
                    }
                )

                Button(
                    onClick = {
                        viewModel.onAction(
                            AccountSetupAction.CompleteSetup
                        )
                    }
                ) {
                    Text(text = "Complete Setup")
                }
            }

            AccountSetupStep.Completed -> {
                Text(
                    text = "Account setup completed."
                )

                Button(
                    onClick = onSetupComplete
                ) {
                    Text(text = "Continue to Login")
                }
            }
        }

        OutlinedButton(
            onClick = onCancel
        ) {
            Text(text = "Cancel")
        }
    }
}
