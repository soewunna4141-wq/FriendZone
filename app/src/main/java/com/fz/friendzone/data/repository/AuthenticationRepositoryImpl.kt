package com.fz.friendzone.data.repository

import com.fz.friendzone.feature.auth.AuthenticationState

/**
 * Provider-independent authentication repository implementation.
 *
 * Real authentication providers, OTP delivery, and session persistence
 * will be connected in later phases.
 */
class AuthenticationRepositoryImpl : AuthenticationRepository {

    private var authenticationState: AuthenticationState =
        AuthenticationState.Unauthenticated

    override fun getAuthenticationState(): AuthenticationState {
        return authenticationState
    }

    override fun requestOtp() {
        authenticationState = AuthenticationState.AwaitingOtp
    }

    override fun signIn(accountId: String) {
        authenticationState = AuthenticationState.Authenticated(
            accountId = accountId
        )
    }

    override fun signOut() {
        authenticationState = AuthenticationState.Unauthenticated
    }
}
