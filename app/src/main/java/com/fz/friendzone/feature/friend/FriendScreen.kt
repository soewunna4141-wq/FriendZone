package com.fz.friendzone.feature.friend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fz.friendzone.R

@Composable
fun FriendScreen(
    viewModel: FriendViewModel
) {
    val uiState = viewModel.uiState.value

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
