package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.data.local.ProfileLocalDataSource

class ProfileRepositoryImpl(
    private val localDataSource: ProfileLocalDataSource
) : ProfileRepository {

    override fun getProfile(): Profile? {
        return localDataSource.getProfile()
    }

    override fun saveProfile(profile: Profile) {
        localDataSource.saveProfile(profile)
    }
}
