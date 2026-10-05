package com.fz.friendzone.feature.content

import android.net.Uri
import androidx.activity.result.ActivityResultCaller
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.fz.friendzone.core.media.AndroidMediaPickerLauncher
import com.fz.friendzone.core.media.CameraCaptureScreen
import com.fz.friendzone.core.media.CameraCaptureUiState
import com.fz.friendzone.core.media.MediaLibrary
import com.fz.friendzone.core.media.MediaStoreMediaStorage
import com.fz.friendzone.core.model.MediaAsset
import com.fz.friendzone.core.model.MediaSource
import com.fz.friendzone.core.model.MediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

@Composable
fun ContentCreationScreen(
    caller: ActivityResultCaller,
    mediaLibrary: MediaLibrary,
    ownerId: String,
    viewModel: ContentCreationViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val mediaPicker = remember(caller) {
        AndroidMediaPickerLauncher(caller)
    }

    val mediaStorage = remember(context) {
        MediaStoreMediaStorage(
            contentResolver = context.contentResolver
        )
    }

    var existingMedia by remember {
        mutableStateOf<List<MediaAsset>>(emptyList())
    }

    var showCamera by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(mediaLibrary, ownerId) {
        existingMedia = withContext(Dispatchers.IO) {
            mediaLibrary.getByOwner(ownerId)
        }
    }

    fun importMedia(
        uri: Uri,
        type: MediaType,
        source: MediaSource
    ) {
        if (
            uiState.target == ContentCreationTarget.REELS &&
            type != MediaType.VIDEO
        ) {
            return
        }

        val mediaId = UUID.randomUUID().toString()

        mediaStorage.save(
            source = uri,
            mediaId = mediaId
        ) { storedUri ->
            if (storedUri == null) return@save

            val asset = MediaAsset(
                id = mediaId,
                ownerId = ownerId,
                uri = storedUri.toString(),
                type = type,
                source = source
            )

            mediaLibrary.add(asset)
            existingMedia = listOf(asset) + existingMedia

            viewModel.onAction(
                ContentCreationAction.MediaSelected(asset)
            )
        }
    }

    if (showCamera) {
        CameraCaptureScreen(
            caller = caller,
            onStateChanged = { state ->
                if (state is CameraCaptureUiState.Captured) {
                    importMedia(
                        uri = state.uri,
                        type = if (state.isVideo) {
                            MediaType.VIDEO
                        } else {
                            MediaType.IMAGE
                        },
                        source = MediaSource.CAMERA
                    )

                    showCamera = false
                }
            },
            modifier = modifier
        )

        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (uiState.target == ContentCreationTarget.NEWS) {
                "Create News Post"
            } else {
                "Create Reel"
            },
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Choose your media source.",
            style = MaterialTheme.typography.bodyMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    mediaPicker.pickImage { uri ->
                        uri?.let {
                            importMedia(
                                uri = it,
                                type = MediaType.IMAGE,
                                source = MediaSource.GALLERY
                            )
                        }
                    }
                },
                enabled = uiState.target == ContentCreationTarget.NEWS,
                modifier = Modifier.weight(1f)
            ) {
                Text("Gallery Image")
            }

            Button(
                onClick = {
                    mediaPicker.pickVideo { uri ->
                        uri?.let {
                            importMedia(
                                uri = it,
                                type = MediaType.VIDEO,
                                source = MediaSource.GALLERY
                            )
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Gallery Video")
            }
        }

        Button(
            onClick = {
                showCamera = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Camera Photo / Video")
        }

        Text(
            text = "FriendZone Existing Media",
            style = MaterialTheme.typography.titleMedium
        )

        if (existingMedia.isEmpty()) {
            Text(
                text = "No reusable FriendZone media yet.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = existingMedia.filter { asset ->
                        uiState.target == ContentCreationTarget.NEWS ||
                            asset.type == MediaType.VIDEO
                    },
                    key = { it.id }
                ) { asset ->
                    ExistingMediaCard(
                        asset = asset,
                        selected = uiState.selectedMedia?.id == asset.id,
                        onClick = {
                            viewModel.onAction(
                                ContentCreationAction.MediaSelected(asset)
                            )
                        }
                    )
                }
            }
        }

        uiState.selectedMedia?.let { selected ->
            Text(
                text = "Selected: " + selected.type.name,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        OutlinedTextField(
            value = uiState.caption,
            onValueChange = { value ->
                viewModel.onAction(
                    ContentCreationAction.CaptionChanged(value)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Caption")
            },
            enabled = !uiState.isLoading
        )

        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = {
                viewModel.onAction(
                    ContentCreationAction.Create
                )
            },
            enabled = !uiState.isLoading && !uiState.isCreated,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (uiState.isLoading) {
                    "Creating..."
                } else {
                    "Create"
                }
            )
        }

        if (uiState.isCreated) {
            Text(
                text = "Content created successfully.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ExistingMediaCard(
    asset: MediaAsset,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = if (selected) {
                    "Selected • " + asset.type.name
                } else {
                    asset.type.name
                },
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = asset.source.name,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
