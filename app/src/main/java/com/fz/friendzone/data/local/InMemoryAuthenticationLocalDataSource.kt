package com.fz.friendzone.data.local

import com.fz.friendzone.feature.auth.AuthenticationState

/**
 * In-memory authentication state data source.
 *
 * Persistent session storage and real authentication providers
 * will be connected in later phases.
 */
class InMemoryAuthenticationLocalDataSource : AuthenticationLocalDataSource {

    private var authenticationState: AuthenticationState =
        AuthenticationState.Unauthenticated

    override fun getAuthenticationState(): AuthenticationState {
        return authenticationState
    }

    override fun saveAuthenticationState(
        state: AuthenticationState
    ) {
        authenticationState = state
    }
}
