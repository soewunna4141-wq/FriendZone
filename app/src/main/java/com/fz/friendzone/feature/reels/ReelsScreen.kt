package com.fz.friendzone.feature.reels

import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.fz.friendzone.R
import com.fz.friendzone.core.model.Reel
import com.fz.friendzone.data.repository.ReelsRepository

@OptIn(ExperimentalFoundationApi::class)
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

    when (val state = uiState) {
        ReelsUiState.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .safeDrawingPadding(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ReelsUiState.Success -> {
            if (state.reels.isEmpty()) {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.reels_count, 0)
                    )
                }
            } else {
                val pagerState = rememberPagerState(
                    initialPage = 0,
                    pageCount = { state.reels.size }
                )

                VerticalPager(
                    state = pagerState,
                    modifier = modifier.fillMaxSize()
                ) { page ->
                    ReelsVideoPage(
                        reel = state.reels[page],
                        isActive = pagerState.currentPage == page
                    )
                }
            }
        }

        is ReelsUiState.Error -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .safeDrawingPadding(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(state.messageResId)
                )
            }
        }
    }
}

@Composable
private fun ReelsVideoPage(
    reel: Reel,
    isActive: Boolean
) {
    val context = LocalContext.current

    val exoPlayer = remember(reel.videoUrl) {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                setMediaItem(
                    MediaItem.fromUri(reel.videoUrl)
                )
                prepare()
                repeatMode = ExoPlayer.REPEAT_MODE_ONE
                playWhenReady = false
            }
    }

    LaunchedEffect(isActive, exoPlayer) {
        if (isActive) {
            exoPlayer.playWhenReady = true
            exoPlayer.play()
        } else {
            exoPlayer.pause()
            exoPlayer.seekTo(0)
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = false
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
