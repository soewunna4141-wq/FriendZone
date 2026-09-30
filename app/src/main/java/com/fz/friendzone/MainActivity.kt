package com.fz.friendzone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fz.friendzone.core.navigation.FriendZoneNavHost

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val friendZoneApplication = application as FriendZoneApplication

        setContent {
            FriendZoneNavHost(
                application = friendZoneApplication
            )
        }
    }
}
