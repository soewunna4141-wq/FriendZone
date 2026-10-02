package com.fz.friendzone.data.repository

import com.fz.friendzone.data.local.AuthenticationLocalDataSource
import com.fz.friendzone.feature.auth.AuthenticationState

/**
 * Provider-independent authentication repository implementation.
 *
 * Real authentication providers, OTP delivery, and session persistence
 * will be connected in later phases.
 */
class AuthenticationRepositoryImpl(
    private val localDataSource: AuthenticationLocalDataSource
) : AuthenticationRepository {

    override fun getAuthenticationState(): AuthenticationState {
        return localDataSource.getAuthenticationState()
    }

    override fun requestOtp() {
        localDataSource.saveAuthenticationState(
            AuthenticationState.AwaitingOtp
        )
    }

    override fun signIn(accountId: String) {
        localDataSource.saveAuthenticationState(
            AuthenticationState.Authenticated(
                accountId = accountId
            )
        )
    }

    override fun signOut() {
        localDataSource.saveAuthenticationState(
            AuthenticationState.Unauthenticated
        )
    }
}
