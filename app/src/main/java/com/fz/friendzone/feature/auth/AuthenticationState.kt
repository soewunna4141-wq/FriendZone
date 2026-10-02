package com.fz.friendzone.feature.auth

/**
 * Provider-independent authentication state.
 *
 * Authentication infrastructure such as real sign-in, sign-up,
 * OTP verification, and session persistence will be connected
 * in later phases.
 */
sealed interface AuthenticationState {

    data object Unauthenticated : AuthenticationState

    data object AwaitingOtp : AuthenticationState

    data class Authenticated(
        val accountId: String
    ) : AuthenticationState
}
