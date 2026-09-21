package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Profile

class FakeProfileLocalDataSource : ProfileLocalDataSource {

    private var profile: Profile? = null

    override fun getProfile(): Profile? {
        return profile
    }

    override fun saveProfile(profile: Profile) {
        this.profile = profile
    }
}
