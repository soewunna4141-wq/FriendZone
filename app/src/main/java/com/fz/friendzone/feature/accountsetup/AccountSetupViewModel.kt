package com.fz.friendzone.feature.accountsetup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AccountSetupStep {
    Setup,
    Verification,
    Completed
}

data class AccountSetupUiState(
    val step: AccountSetupStep = AccountSetupStep.Setup,
    val phoneNumber: String = "",
    val verificationCode: String = ""
)

sealed interface AccountSetupAction {

    data class PhoneNumberChanged(
        val value: String
    ) : AccountSetupAction

    data object RequestVerification : AccountSetupAction

    data class VerificationCodeChanged(
        val value: String
    ) : AccountSetupAction

    data object CompleteSetup : AccountSetupAction
}

class AccountSetupViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        AccountSetupUiState()
    )

    val uiState: StateFlow<AccountSetupUiState> =
        _uiState.asStateFlow()

    fun onAction(action: AccountSetupAction) {
        when (action) {
            is AccountSetupAction.PhoneNumberChanged -> {
                _uiState.value = _uiState.value.copy(
                    phoneNumber = action.value
                )
            }

            AccountSetupAction.RequestVerification -> {
                _uiState.value = _uiState.value.copy(
                    step = AccountSetupStep.Verification
                )
            }

            is AccountSetupAction.VerificationCodeChanged -> {
                _uiState.value = _uiState.value.copy(
                    verificationCode = action.value
                )
            }

            AccountSetupAction.CompleteSetup -> {
                _uiState.value = _uiState.value.copy(
                    step = AccountSetupStep.Completed
                )
            }
        }
    }
}
