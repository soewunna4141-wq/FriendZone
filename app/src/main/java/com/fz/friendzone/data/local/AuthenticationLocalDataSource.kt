package com.fz.friendzone.data.local

import com.fz.friendzone.feature.auth.AuthenticationState

interface AuthenticationLocalDataSource {

    fun getAuthenticationState(): AuthenticationState

    fun saveAuthenticationState(
        state: AuthenticationState
    )
}
