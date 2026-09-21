package com.fz.friendzone.data.repository

import com.fz.friendzone.core.model.Profile

interface ProfileRepository {

    fun getProfile(): Profile?

    fun saveProfile(profile: Profile)
}
