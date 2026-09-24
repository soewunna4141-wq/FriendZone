package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Profile

class InMemoryProfileLocalDataSource : ProfileLocalDataSource {

    private var profile: Profile? = null

    override fun getProfile(): Profile? {
        return profile
    }

    override fun getProfile(userId: String): Profile? {
        return profile?.takeIf { storedProfile ->
            storedProfile.userId == userId
        }
    }

    override fun saveProfile(profile: Profile) {
        this.profile = profile
    }
}
