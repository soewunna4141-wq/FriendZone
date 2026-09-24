package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.data.local.ProfileLocalDataSource

class ProfileRepositoryImpl(
    private val localDataSource: ProfileLocalDataSource
) : ProfileRepository {

    override fun getProfile(): Profile? {
        return localDataSource.getProfile()
    }

    override fun getProfile(userId: String): Profile? {
        return localDataSource.getProfile()?.takeIf { profile ->
            profile.userId == userId
        }
    }

    override fun saveProfile(profile: Profile) {
        localDataSource.saveProfile(profile)
    }
}
