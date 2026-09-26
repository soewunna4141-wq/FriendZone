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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.fz.friendzone.core.model.Reaction
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.ProfileRepository
import com.fz.friendzone.data.repository.ReactionRepository
import java.text.DateFormat
import java.util.Date
import java.util.UUID

@Composable
fun NewsScreen(
    repository: NewsRepository,
    profileRepository: ProfileRepository,
    reactionRepository: ReactionRepository
) {
    val viewModel: NewsViewModel = viewModel(
        factory = NewsViewModelFactory(
            newsRepository = repository,
            profileRepository = profileRepository,
            reactionRepository = reactionRepository
        )
    )

    val uiState by viewModel.uiState.collectAsState()

    var caption by remember {
        mutableStateOf("")
    }

    val currentProfile = viewModel.currentProfile

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
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                NewsCreatePost(
                    caption = caption,
                    onCaptionChange = { caption = it },
                    enabled = currentProfile != null,
                    onCreatePost = {
                        val trimmedCaption = caption.trim()
                        val userId = currentProfile?.userId

                        if (trimmedCaption.isNotEmpty() && !userId.isNullOrBlank()) {
                            viewModel.onAction(
                                NewsAction.CreatePost(
                                    Post(
                                        id = UUID.randomUUID().toString(),
                                        userId = userId,
                                        caption = trimmedCaption,
                                        createdAt = System.currentTimeMillis()
                                    )
                                )
                            )

                            caption = ""
                        }
                    }
                )

                NewsPostList(
                    posts = state.posts,
                    currentUserId = currentProfile?.userId,
                    onLikePost = { postId, userId ->
                        viewModel.onAction(
                            NewsAction.SaveReaction(
                                Reaction(
                                    id = UUID.randomUUID().toString(),
                                    postId = postId,
                                    userId = userId,
                                    type = "LIKE"
                                )
                            )
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }
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
private fun NewsCreatePost(
    caption: String,
    onCaptionChange: (String) -> Unit,
    enabled: Boolean,
    onCreatePost: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = caption,
                onValueChange = onCaptionChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(R.string.news_create_post_caption)
                    )
                },
                singleLine = false,
                enabled = enabled
            )

            Button(
                onClick = onCreatePost,
                enabled = enabled && caption.trim().isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.news_create_post_button)
                )
            }
        }
    }
}

@Composable
private fun NewsPostList(
    posts: List<NewsPostUiModel>,
    currentUserId: String?,
    onLikePost: (postId: String, userId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (posts.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.news_no_posts)
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = posts,
            key = { postUiModel -> postUiModel.post.id }
        ) { postUiModel ->
            NewsPostCard(
                post = postUiModel.post,
                profile = postUiModel.profile,
                currentUserId = currentUserId,
                reactionCount = postUiModel.reactionCount,
                onLikePost = onLikePost
            )
        }
    }
}

@Composable
private fun NewsPostCard(
    post: Post,
    profile: Profile?,
    currentUserId: String?,
    reactionCount: Int,
    onLikePost: (postId: String, userId: String) -> Unit
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
                post = post
            )

            Button(
                onClick = {
                    val userId = currentUserId

                    if (!userId.isNullOrBlank()) {
                        onLikePost(
                            post.id,
                            userId
                        )
                    }
                },
                enabled = !currentUserId.isNullOrBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.news_reaction_like)
                )
            }

            Text(
                text = reactionCount.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
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
    post: Post
) {
    Text(
        text = post.caption,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 8.dp)
    )

    Text(
        text = DateFormat.getDateTimeInstance(
            DateFormat.MEDIUM,
            DateFormat.SHORT
        ).format(Date(post.createdAt)),
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(top = 6.dp)
    )

    if (!post.mediaUrl.isNullOrBlank()) {
        AsyncImage(
            model = post.mediaUrl,
            contentDescription = post.caption,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
    }
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
