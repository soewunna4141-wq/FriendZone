package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Profile

interface ProfileLocalDataSource {

    fun getProfile(): Profile?

    fun getProfile(userId: String): Profile? {
        return getProfile()?.takeIf { profile ->
            profile.userId == userId
        }
    }

    fun saveProfile(profile: Profile)
}
