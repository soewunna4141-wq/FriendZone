package com.fz.friendzone.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
                authenticationRepository.getAuthenticationState()
            }
        }
    }
}

@Composable
fun LoginScreen(
    viewModelFactory: LoginViewModelFactory,
    onLogin: () -> Unit = {},
    onBiometric: () -> Unit = {},
    onSignUp: () -> Unit = {}
) {
    val viewModel: LoginViewModel = viewModel(
        factory = viewModelFactory
    )

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "FriendZone")
        Text(text = "Login")

        OutlinedTextField(
            value = uiState.phoneNumber,
            onValueChange = {
                viewModel.onAction(
                    LoginAction.PhoneNumberChanged(it)
                )
            },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = uiState.password,
            onValueChange = {
                viewModel.onAction(
                    LoginAction.PasswordChanged(it)
                )
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            singleLine = true
        )

        Button(
            onClick = onLogin,
            enabled = uiState.phoneNumber.isNotBlank() &&
                uiState.password.isNotBlank()
        ) {
            Text(text = "Login")
        }

        OutlinedButton(
            onClick = onBiometric
        ) {
            Text(text = "Biometric")
        }

        OutlinedButton(
            onClick = onSignUp
        ) {
            Text(text = "Sign Up")
        }
    }
}
