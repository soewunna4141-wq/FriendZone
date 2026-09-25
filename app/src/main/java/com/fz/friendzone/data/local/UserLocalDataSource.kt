package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.User

interface UserLocalDataSource {

    fun getCurrentUser(): User?

    fun getUser(userId: String): User?
}
