package com.fz.friendzone.feature.login

import androidx.lifecycle.ViewModel
import com.fz.friendzone.data.repository.AuthenticationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoginUiState(
    val phoneNumber: String = "",
    val password: String = ""
)

sealed interface LoginAction {
    data class PhoneNumberChanged(val value: String) : LoginAction
    data class PasswordChanged(val value: String) : LoginAction
    data object Login : LoginAction
    data object Biometric : LoginAction
    data object SignUp : LoginAction
}

class LoginViewModel(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.PhoneNumberChanged -> {
                _uiState.value = _uiState.value.copy(
                    phoneNumber = action.value
                )
            }

            is LoginAction.PasswordChanged -> {
                _uiState.value = _uiState.value.copy(
                    password = action.value
                )
            }

            LoginAction.Login,
            LoginAction.Biometric,
            LoginAction.SignUp -> {
                // Authentication behavior will be connected
                // in later authentication phases.
                authenticationRepository.getAuthenticationState()
            }
        }
    }
}
