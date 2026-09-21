package com.fz.friendzone.feature.profile

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fz.friendzone.app.FriendZoneDependencies
import com.fz.friendzone.core.model.Profile

@Composable
fun ProfileScreen(
    dependencies: FriendZoneDependencies
) {
    val viewModel: ProfileViewModel = viewModel(
        factory = dependencies.profileViewModelFactory
    )

    val uiState by viewModel.uiState.collectAsState()

    val profile = uiState.profile

    if (profile == null) {
        Text(text = "No profile")

        Button(
            onClick = {
                viewModel.onAction(
                    ProfileAction.Save(
                        Profile(
                            userId = "demo-user",
                            displayName = "Demo User",
                            bio = "Welcome to FriendZone"
                        )
                    )
                )
            }
        ) {
            Text(text = "Create Demo Profile")
        }
    } else {
        Text(
            text = "Display Name: ${profile.displayName}\n" +
                "Bio: ${profile.bio ?: ""}\n" +
                "User ID: ${profile.userId}"
        )

        Button(
            onClick = {
                viewModel.onAction(ProfileAction.Load)
            }
        ) {
            Text(text = "Reload Profile")
        }
    }
}
