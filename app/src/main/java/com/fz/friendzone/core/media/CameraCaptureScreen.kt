package com.fz.friendzone.core.media

import android.content.Context
import androidx.activity.result.ActivityResultCaller
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraCaptureScreen(
    caller: ActivityResultCaller,
    onStateChanged: (CameraCaptureUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var state by remember {
        mutableStateOf<CameraCaptureUiState>(
            CameraCaptureUiState.Idle
        )
    }

    val previewView = remember {
        PreviewView(context)
    }

    val permissionRequester = remember(caller) {
        CameraPermissionRequester(caller)
    }

    val cameraManager = remember(
        context,
        lifecycleOwner,
        previewView
    ) {
        CameraXCaptureManager(
            context = context,
            lifecycleOwner = lifecycleOwner,
            previewView = previewView
        )
    }

    fun updateState(
        newState: CameraCaptureUiState
    ) {
        state = newState
        onStateChanged(newState)
    }

    LaunchedEffect(Unit) {
        if (CameraPermission.isGranted(context)) {
            updateState(CameraCaptureUiState.Ready)

            cameraManager.startCamera(
                onReady = {
                    updateState(CameraCaptureUiState.Ready)
                },
                onError = { throwable ->
                    updateState(
                        CameraCaptureUiState.Error(
                            throwable.message ?: "Unable to start camera"
                        )
                    )
                }
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraManager.stopVideoCapture()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        AndroidView(
            factory = {
                previewView
            },
            modifier = Modifier.weight(1f)
        )

        when (state) {
            CameraCaptureUiState.Idle -> {
                Button(
                    onClick = {
                        updateState(
                            CameraCaptureUiState.RequestingPermission
                        )

                        permissionRequester.request { granted ->
                            if (granted) {
                                cameraManager.startCamera(
                                    onReady = {
                                        updateState(
                                            CameraCaptureUiState.Ready
                                        )
                                    },
                                    onError = { throwable ->
                                        updateState(
                                            CameraCaptureUiState.Error(
                                                throwable.message
                                                    ?: "Unable to start camera"
                                            )
                                        )
                                    }
                                )
                            } else {
                                updateState(
                                    CameraCaptureUiState.Error(
                                        "Camera permission denied"
                                    )
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Allow Camera")
                }
            }

            CameraCaptureUiState.RequestingPermission -> {
                Text(
                    text = "Requesting camera permission",
                    modifier = Modifier.padding(16.dp)
                )
            }

            CameraCaptureUiState.Ready -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = {
                            updateState(
                                CameraCaptureUiState.Error(
                                    "Photo capture requested"
                                )
                            )

                            cameraManager.captureImage { uri ->
                                if (uri != null) {
                                    updateState(
                                        CameraCaptureUiState.Captured(
                                            uri = uri,
                                            isVideo = false
                                        )
                                    )
                                } else {
                                    updateState(
                                        CameraCaptureUiState.Error(
                                            "Photo capture failed"
                                        )
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Take Photo")
                    }

                    Button(
                        onClick = {
                            updateState(
                                CameraCaptureUiState.Recording
                            )

                            cameraManager.startVideoCapture { uri ->
                                if (uri != null) {
                                    updateState(
                                        CameraCaptureUiState.Captured(
                                            uri = uri,
                                            isVideo = true
                                        )
                                    )
                                } else {
                                    updateState(
                                        CameraCaptureUiState.Error(
                                            "Video capture failed"
                                        )
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text("Start Video")
                    }
                }
            }

            CameraCaptureUiState.Recording -> {
                Button(
                    onClick = {
                        cameraManager.stopVideoCapture()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Stop Video")
                }
            }

            is CameraCaptureUiState.Captured -> {
                Button(
                    onClick = {
                        updateState(
                            CameraCaptureUiState.Ready
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Continue")
                }
            }

            is CameraCaptureUiState.Error -> {
                Button(
                    onClick = {
                        updateState(
                            CameraCaptureUiState.Idle
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Try Again")
                }
            }
        }
    }
}
