package com.fz.friendzone.feature.reels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fz.friendzone.data.repository.ReelsRepository

@Composable
fun ReelsScreen(
    repository: ReelsRepository,
    modifier: Modifier = Modifier
) {
    val viewModel: ReelsViewModel = viewModel(
        factory = ReelsViewModelFactory(repository)
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onAction(ReelsAction.Load)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = uiState) {
            ReelsUiState.Loading -> {
                Text(text = "Loading...")
            }

            is ReelsUiState.Success -> {
                Text(text = "Reels: ${state.reels.size}")
            }

            is ReelsUiState.Error -> {
                Text(text = stringResource(state.messageResId))
            }
        }
    }
}
