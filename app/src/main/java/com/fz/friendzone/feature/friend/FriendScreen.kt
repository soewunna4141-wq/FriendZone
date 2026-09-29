package com.fz.friendzone.feature.friend

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
fun FriendScreen(
    dependencies: FriendZoneDependencies
) {
    val viewModel: FriendViewModel = viewModel(
        factory = dependencies.friendViewModelFactory
    )

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.friend_screen_title)
        )

        uiState.friends.forEach { friend ->
            Text(
                text = stringResource(
                    R.string.friend_relationship_format,
                    friend.userId,
                    friend.friendUserId
                )
            )
        }
    }
}
