package com.fz.friendzone

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

private const val ROUTE_COMPOSER = "composer"

@Composable
fun FriendZoneNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ROUTE_COMPOSER
    ) {
        composable(ROUTE_COMPOSER) {
            ComposerScreen()
        }
    }
}
