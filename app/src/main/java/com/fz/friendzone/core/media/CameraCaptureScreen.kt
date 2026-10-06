package com.fz.friendzone.core.media

import android.app.Activity
import androidx.activity.result.ActivityResultCaller
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.camera.view.PreviewView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner

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

    val cameraPermissionRequester = remember(caller) {
        CameraPermissionRequester(caller)
    }

    val microphonePermissionRequester = remember(caller) {
        MicrophonePermissionRequester(caller)
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

    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (context is Activity) {
            cameraPermissionRequester.request {
                updateState(
                    CameraCaptureUiState.RequestingPermission
                )

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
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = {
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        when (val currentState = state) {
            CameraCaptureUiState.Idle -> Unit

            CameraCaptureUiState.RequestingPermission -> {
                Text(
                    text = "Opening camera...",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }

            CameraCaptureUiState.Ready -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            cameraManager.captureImage { uri ->
                                if (uri != null) {
                                    updateState(
                                        CameraCaptureUiState.Captured(uri)
                                    )
                                } else {
                                    updateState(
                                        CameraCaptureUiState.Error(
                                            "Unable to capture photo"
                                        )
                                    )
                                }
                            }
                        }
                    ) {
                        Text("Take Photo")
                    }

                    Button(
                        onClick = {
                            microphonePermissionRequester.request {
                                cameraManager.startVideoCapture { uri ->
                                    if (uri != null) {
                                        updateState(
                                            CameraCaptureUiState.Captured(uri)
                                        )
                                    } else {
                                        updateState(
                                            CameraCaptureUiState.Error(
                                                "Unable to capture video"
                                            )
                                        )
                                    }
                                }

                                updateState(
                                    CameraCaptureUiState.Recording
                                )
                            }
                        }
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
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(16.dp)
                ) {
                    Text("Stop Video")
                }
            }

            is CameraCaptureUiState.Captured -> {
                Button(
                    onClick = {
                        updateState(currentState)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(16.dp)
                ) {
                    Text("Continue")
                }
            }

            is CameraCaptureUiState.Error -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentState.message,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Button(
                        onClick = {
                            updateState(
                                CameraCaptureUiState.RequestingPermission
                            )

                            cameraPermissionRequester.request {
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
                            }
                        }
                    ) {
                        Text("Try Again")
                    }
                }
            }
        }
    }
}
