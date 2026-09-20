package com.fz.friendzone.feature.share

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fz.friendzone.R

/**
 * Main screen: type a caption, optionally attach media, tick which
 * platforms to send to, then hit "Post". Each ticked platform opens
 * its own share sheet one after another (see ShareDispatcher).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposerScreen() {
    val context = LocalContext.current

    var caption by remember { mutableStateOf("") }
    var mediaUri by remember { mutableStateOf<Uri?>(null) }
    var mediaMime by remember { mutableStateOf<String?>(null) }

    val selectedPlatforms = remember {
        mutableStateMapOf(
            Platform.FACEBOOK to true,
            Platform.TIKTOK to false,
            Platform.TELEGRAM to true
        )
    }

    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            mediaUri = uri
            mediaMime = context.contentResolver.getType(uri)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.composer_title))
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = caption,
                onValueChange = { caption = it },
                label = {
                    Text(stringResource(R.string.composer_hint))
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = {
                        pickMedia.launch("*/*")
                    }
                ) {
                    Text(
                        if (mediaUri == null) {
                            stringResource(R.string.add_media)
                        } else {
                            stringResource(R.string.change_media)
                        }
                    )
                }

                Spacer(Modifier.width(12.dp))

                if (mediaUri != null) {
                    Text(
                        stringResource(R.string.media_attached),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            HorizontalDivider()

            Text(
                stringResource(R.string.post_to),
                style = MaterialTheme.typography.titleMedium
            )

            Platform.entries.forEach { platform ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = selectedPlatforms[platform] == true,
                        onCheckedChange = { checked ->
                            selectedPlatforms[platform] = checked
                        }
                    )

                    Text(platform.label)
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    val chosen =
                        selectedPlatforms
                            .filterValues { it }
                            .keys
                            .toList()

                    if (
                        chosen.isEmpty() ||
                        (caption.isBlank() && mediaUri == null)
                    ) {
                        return@Button
                    }

                    val content = PostContent(
                        caption = caption,
                        mediaUri = mediaUri,
                        mediaMimeType = mediaMime
                    )

                    ShareDispatcher.shareToAll(
                        context,
                        chosen,
                        content
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.post))
            }

            Text(
                stringResource(R.string.share_info),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
