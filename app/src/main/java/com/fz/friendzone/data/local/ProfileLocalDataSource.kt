package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Profile

interface ProfileLocalDataSource {

    fun getProfile(): Profile?

    fun saveProfile(profile: Profile)
}
