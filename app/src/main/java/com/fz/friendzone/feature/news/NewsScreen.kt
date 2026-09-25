package com.fz.friendzone.feature.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fz.friendzone.R
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.ProfileRepository

@Composable
fun NewsScreen(
    repository: NewsRepository,
    profileRepository: ProfileRepository
) {
    val viewModel: NewsViewModel = viewModel(
        factory = NewsViewModelFactory(
            newsRepository = repository,
            profileRepository = profileRepository
        )
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onAction(NewsAction.Load)
    }

    when (val state = uiState) {
        NewsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is NewsUiState.Success -> {
            NewsPostList(posts = state.posts)
        }

        is NewsUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(state.messageResId),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun NewsPostList(
    posts: List<NewsPostUiModel>
) {
    if (posts.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.news_no_posts)
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = posts,
            key = { postUiModel -> postUiModel.post.id }
        ) { postUiModel ->
            NewsPostCard(
                post = postUiModel.post,
                profile = postUiModel.profile
            )
        }
    }
}

@Composable
private fun NewsPostCard(
    post: Post,
    profile: Profile?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (profile != null) {
                NewsAuthorHeader(
                    profile = profile
                )
            }

            NewsPostContent(
                caption = post.caption
            )
        }
    }
}

@Composable
private fun NewsAuthorHeader(
    profile: Profile
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        NewsAuthorAvatar(
            displayName = profile.displayName,
            profileImageUrl = profile.profileImageUrl
        )

        Text(
            text = profile.displayName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
private fun NewsPostContent(
    caption: String
) {
    Text(
        text = caption,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun NewsAuthorAvatar(
    displayName: String,
    profileImageUrl: String?
) {
    val initial = displayName
        .trim()
        .firstOrNull()
        ?.uppercaseChar()
        ?.toString()
        ?: "?"

    if (profileImageUrl.isNullOrBlank()) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial,
                style = MaterialTheme.typography.titleMedium
            )
        }
    } else {
        AsyncImage(
            model = profileImageUrl,
            contentDescription = displayName,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}
