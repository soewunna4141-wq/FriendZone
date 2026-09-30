package com.fz.friendzone.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fz.friendzone.R
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (profile == null) {
            Text(
                text = stringResource(R.string.profile_no_profile)
            )

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
                Text(
                    text = stringResource(R.string.profile_create_demo)
                )
            }
        } else {
            Text(
                text = stringResource(
                    R.string.profile_display_name,
                    profile.displayName
                ) + "\n" +
                    stringResource(
                        R.string.profile_bio,
                        profile.bio ?: ""
                    ) + "\n" +
                    stringResource(
                        R.string.profile_user_id,
                        profile.userId
                    )
            )

            Button(
                onClick = {
                    viewModel.onAction(ProfileAction.Load)
                }
            ) {
                Text(
                    text = stringResource(R.string.profile_reload)
                )
            }
        }
    }
}
