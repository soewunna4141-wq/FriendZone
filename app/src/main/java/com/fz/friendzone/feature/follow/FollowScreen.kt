package com.fz.friendzone.feature.follow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fz.friendzone.R
import com.fz.friendzone.app.FriendZoneDependencies

@Composable
fun FollowScreen(
    dependencies: FriendZoneDependencies
) {
    val viewModel: FollowViewModel = viewModel(
        factory = dependencies.followViewModelFactory
    )

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.follow_screen_title)
        )

        uiState.followers.forEach { follow ->
            Text(
                text = stringResource(
                    R.string.follow_follower_format,
                    follow.followerId,
                    follow.followingId
                )
            )
        }

        uiState.following.forEach { follow ->
            Text(
                text = stringResource(
                    R.string.follow_following_format,
                    follow.followerId,
                    follow.followingId
                )
            )
        }
    }
}
