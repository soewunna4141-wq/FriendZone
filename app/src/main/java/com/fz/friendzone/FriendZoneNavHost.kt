package com.fz.friendzone.core.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun FriendZoneNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.COMPOSER
    ) {
        composable(NavigationRoutes.COMPOSER) {
            ComposerScreen()
        }
    }
}
