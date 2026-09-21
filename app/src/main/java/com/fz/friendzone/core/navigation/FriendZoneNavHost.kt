package com.fz.friendzone.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fz.friendzone.FriendZoneApplication
import com.fz.friendzone.feature.account.AccountScreen
import com.fz.friendzone.feature.share.ComposerScreen

@Composable
fun FriendZoneNavHost(
    application: FriendZoneApplication
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.COMPOSER
    ) {
        composable(NavigationRoutes.COMPOSER) {
            ComposerScreen()
        }

        composable(NavigationRoutes.ACCOUNT) {
            AccountScreen(
                dependencies = application.dependencies
            )
        }
    }
}
