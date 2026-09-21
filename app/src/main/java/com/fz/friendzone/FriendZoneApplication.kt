package com.fz.friendzone

import android.app.Application
import com.fz.friendzone.app.FriendZoneDependencies

class FriendZoneApplication : Application() {

    val dependencies: FriendZoneDependencies by lazy {
        FriendZoneDependencies()
    }
}
