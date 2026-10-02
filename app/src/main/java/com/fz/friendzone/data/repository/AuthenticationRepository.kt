package com.fz.friendzone.data.repository

import com.fz.friendzone.feature.auth.AuthenticationState

/**
 * Provider-independent authentication boundary.
 *
 * Real authentication providers, OTP delivery, session persistence,
 * and credential handling will be connected in later phases.
 */
interface AuthenticationRepository {

    fun getAuthenticationState(): AuthenticationState

    fun requestOtp()

    fun signIn(accountId: String)

    fun signOut()
}
