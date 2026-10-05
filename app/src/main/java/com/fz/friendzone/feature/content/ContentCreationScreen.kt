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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fz.friendzone.core.media.AndroidMediaPickerLauncher
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
    onMediaSelected: (MediaAsset) -> Unit,
    onCameraRequested: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mediaPicker = remember(caller) {
        AndroidMediaPickerLauncher(caller)
    }

    val mediaStorage = remember {
        MediaStoreMediaStorage()
    }

    var existingMedia by remember {
        mutableStateOf<List<MediaAsset>>(emptyList())
    }

    LaunchedEffect(mediaLibrary, ownerId) {
        existingMedia = withContext(Dispatchers.IO) {
            mediaLibrary.getByOwner(ownerId)
        }
    }

    fun importGalleryMedia(
        uri: Uri,
        type: MediaType
    ) {
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
                source = MediaSource.GALLERY
            )

            mediaLibrary.add(asset)
            existingMedia = listOf(asset) + existingMedia
            onMediaSelected(asset)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Create Content",
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
                            importGalleryMedia(
                                uri = it,
                                type = MediaType.IMAGE
                            )
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Gallery Image")
            }

            Button(
                onClick = {
                    mediaPicker.pickVideo { uri ->
                        uri?.let {
                            importGalleryMedia(
                                uri = it,
                                type = MediaType.VIDEO
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
            onClick = onCameraRequested,
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
                    items = existingMedia,
                    key = { it.id }
                ) { asset ->
                    ExistingMediaCard(
                        asset = asset,
                        onClick = {
                            onMediaSelected(asset)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExistingMediaCard(
    asset: MediaAsset,
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
                text = asset.type.name,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = asset.source.name,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
