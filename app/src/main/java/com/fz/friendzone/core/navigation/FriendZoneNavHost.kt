package com.fz.friendzone.core.navigation

import android.app.Activity
import androidx.activity.ActivityResultCaller
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fz.friendzone.FriendZoneApplication
import com.fz.friendzone.feature.account.AccountScreen
import com.fz.friendzone.feature.accountsetup.AccountSetupScreen
import com.fz.friendzone.feature.accountsetup.AccountSetupViewModel
import com.fz.friendzone.feature.accountsetup.AccountSetupViewModelFactory
import com.fz.friendzone.feature.choice.ChoiceScreen
import com.fz.friendzone.feature.content.ContentCreationScreen
import com.fz.friendzone.feature.content.ContentCreationTarget
import com.fz.friendzone.feature.content.ContentCreationViewModel
import com.fz.friendzone.feature.content.ContentCreationViewModelFactory
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
    val context = LocalContext.current

    val activityResultCaller = context as? ActivityResultCaller
    val activity = context as? Activity

    val ownerId = remember {
        activity?.let { it.packageName } ?: "friendzone_user"
    }

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.LOGIN
    ) {
        composable(NavigationRoutes.LOGIN) {
            LoginScreen(
                viewModelFactory = application.dependencies.loginViewModelFactory,
                onLogin = {
                    navController.navigate(NavigationRoutes.CHOICE)
                },
                onSignUp = {
                    navController.navigate(
                        NavigationRoutes.ACCOUNT_SETUP
                    )
                }
            )
        }

        composable(NavigationRoutes.CHOICE) {
            ChoiceScreen(
                onNewsSelected = {
                    navController.navigate(NavigationRoutes.NEWS)
                },
                onReelsSelected = {
                    navController.navigate(NavigationRoutes.REELS)
                }
            )
        }

        composable(NavigationRoutes.ACCOUNT_SETUP) {
            AccountSetupScreen(
                viewModel = AccountSetupViewModelFactory().create(
                    AccountSetupViewModel::class.java
                ),
                onSetupComplete = {
                    navController.popBackStack(
                        NavigationRoutes.LOGIN,
                        inclusive = false
                    )
                },
                onCancel = {
                    navController.popBackStack()
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
                    navController.navigate(NavigationRoutes.CHOICE)
                },
                onProfileClick = {
                    navController.navigate(NavigationRoutes.PROFILE)
                },
                onFriendClick = {
                    navController.navigate(NavigationRoutes.FRIEND)
                },
                onFollowClick = {
                    navController.navigate(NavigationRoutes.FOLLOW)
                },
                onComposerClick = {
                    navController.navigate(
                        "${NavigationRoutes.COMPOSER}_news"
                    )
                }
            ) {
                NewsScreen(
                    repository = application.dependencies.newsRepository,
                    profileRepository = application.dependencies.profileRepository,
                    reactionRepository = application.dependencies.reactionRepository,
                    commentRepository = application.dependencies.commentRepository,
                    mediaLibrary = application.dependencies.mediaLibrary
                )
            }
        }

        composable("${NavigationRoutes.COMPOSER}_news") {
            if (activityResultCaller == null) {
                navController.popBackStack()
            } else {
                val viewModel = remember {
                    ContentCreationViewModelFactory(
                        newsRepository = application.dependencies.newsRepository,
                        reelsRepository = application.dependencies.reelsRepository,
                        target = ContentCreationTarget.NEWS
                    ).create(ContentCreationViewModel::class.java)
                }

                ContentCreationScreen(
                    caller = activityResultCaller,
                    mediaLibrary = application.dependencies.mediaLibrary,
                    ownerId = ownerId,
                    viewModel = viewModel
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
                viewModelFactory = application.dependencies.followViewModelFactory
            )
        }

        composable(NavigationRoutes.REELS) {
            PrimaryExperienceShell(
                onLogoClick = {
                    navController.navigate(NavigationRoutes.CHOICE)
                },
                onProfileClick = {
                    navController.navigate(NavigationRoutes.PROFILE)
                },
                onFriendClick = {
                    navController.navigate(NavigationRoutes.FRIEND)
                },
                onFollowClick = {
                    navController.navigate(NavigationRoutes.FOLLOW)
                },
                onComposerClick = {
                    navController.navigate(
                        "${NavigationRoutes.COMPOSER}_reels"
                    )
                }
            ) {
                ReelsScreen(
                    repository = application.dependencies.reelsRepository
                )
            }
        }

        composable("${NavigationRoutes.COMPOSER}_reels") {
            if (activityResultCaller == null) {
                navController.popBackStack()
            } else {
                val viewModel = remember {
                    ContentCreationViewModelFactory(
                        newsRepository = application.dependencies.newsRepository,
                        reelsRepository = application.dependencies.reelsRepository,
                        target = ContentCreationTarget.REELS
                    ).create(ContentCreationViewModel::class.java)
                }

                ContentCreationScreen(
                    caller = activityResultCaller,
                    mediaLibrary = application.dependencies.mediaLibrary,
                    ownerId = ownerId,
                    viewModel = viewModel
                )
            }
        }
    }
}
