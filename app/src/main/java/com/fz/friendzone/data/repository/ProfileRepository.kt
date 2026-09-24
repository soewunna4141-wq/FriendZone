package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Profile

interface ProfileRepository {

    fun getProfile(): Profile?

    fun getProfile(userId: String): Profile? {
        return getProfile()?.takeIf { profile ->
            profile.userId == userId
        }
    }

    fun saveProfile(profile: Profile)
}
