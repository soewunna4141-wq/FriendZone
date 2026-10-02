package com.fz.friendzone.data.repository

import com.fz.friendzone.data.local.InMemoryAuthenticationLocalDataSource

/**
 * Factory for creating the provider-independent authentication repository.
 *
 * The concrete in-memory local data source is intentionally hidden
 * behind the repository boundary.
 */
object AuthenticationRepositoryFactory {

    fun create(): AuthenticationRepository {
        val localDataSource = InMemoryAuthenticationLocalDataSource()

        return AuthenticationRepositoryImpl(
            localDataSource = localDataSource
        )
    }
}
