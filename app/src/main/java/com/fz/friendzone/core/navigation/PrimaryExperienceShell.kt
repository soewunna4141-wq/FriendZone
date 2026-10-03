package com.fz.friendzone.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fz.friendzone.FriendZoneApplication
import com.fz.friendzone.feature.account.AccountScreen
import com.fz.friendzone.feature.choice.ChoiceScreen
import com.fz.friendzone.feature.follow.FollowScreen
import com.fz.friendzone.feature.friend.FriendScreen
import com.fz.friendzone.feature.login.LoginScreen
import com.fz.friendzone.feature.news.NewsScreen
import com.fz.friendzone.feature.profile.ProfileScreen
import com.fz.friendzone.feature.reels.ReelsScreen
import com.fz.friendzone.feature.share.ComposerScreen

@Composable
fun FriendZoneNavHost(
    application: FriendZoneApplication
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.LOGIN
    ) {
        composable(NavigationRoutes.LOGIN) {
            LoginScreen(
                viewModelFactory =
                    application.dependencies.loginViewModelFactory,
                onLogin = {
                    navController.navigate(
                        NavigationRoutes.CHOICE
                    )
                }
            )
        }

        composable(NavigationRoutes.CHOICE) {
            ChoiceScreen(
                onNewsSelected = {
                    navController.navigate(
                        NavigationRoutes.NEWS
                    )
                },
                onReelsSelected = {
                    navController.navigate(
                        NavigationRoutes.REELS
                    )
                }
            )
        }

        composable(NavigationRoutes.COMPOSER) {
            ComposerScreen()
        }

        composable(NavigationRoutes.ACCOUNT) {
            AccountScreen(
                dependencies = application.dependencies
            )
        }

        composable(NavigationRoutes.PROFILE) {
            ProfileScreen(
                dependencies = application.dependencies
            )
        }

        composable(NavigationRoutes.NEWS) {
            PrimaryExperienceShell(
                onLogoClick = {
                    navController.navigate(
                        NavigationRoutes.CHOICE
                    )
                },
                onProfileClick = {
                    navController.navigate(
                        NavigationRoutes.PROFILE
                    )
                },
                onFriendClick = {
                    navController.navigate(
                        NavigationRoutes.FRIEND
                    )
                },
                onFollowClick = {
                    navController.navigate(
                        NavigationRoutes.FOLLOW
                    )
                },
                onComposerClick = {
                    navController.navigate(
                        NavigationRoutes.COMPOSER
                    )
                }
            ) {
                NewsScreen(
                    repository =
                        application.dependencies.newsRepository,
                    profileRepository =
                        application.dependencies.profileRepository,
                    reactionRepository =
                        application.dependencies.reactionRepository,
                    commentRepository =
                        application.dependencies.commentRepository
                )
            }
        }

        composable(NavigationRoutes.FRIEND) {
            FriendScreen(
                dependencies = application.dependencies
            )
        }

        composable(NavigationRoutes.FOLLOW) {
            FollowScreen(
                viewModelFactory =
                    application.dependencies.followViewModelFactory
            )
        }

        composable(NavigationRoutes.REELS) {
            PrimaryExperienceShell(
                onLogoClick = {
                    navController.navigate(
                        NavigationRoutes.CHOICE
                    )
                },
                onProfileClick = {
                    navController.navigate(
                        NavigationRoutes.PROFILE
                    )
                },
                onFriendClick = {
                    navController.navigate(
                        NavigationRoutes.FRIEND
                    )
                },
                onFollowClick = {
                    navController.navigate(
                        NavigationRoutes.FOLLOW
                    )
                },
                onComposerClick = {
                    navController.navigate(
                        NavigationRoutes.COMPOSER
                    )
                }
            ) {
                ReelsScreen(
                    repository =
                        application.dependencies.reelsRepository
                )
            }
        }
    }
}
