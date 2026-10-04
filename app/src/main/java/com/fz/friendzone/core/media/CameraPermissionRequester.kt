package com.fz.friendzone.core.media

import android.Manifest
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts

class CameraPermissionRequester(
    caller: ActivityResultCaller
) {

    private var onResult: ((Boolean) -> Unit)? = null

    private val launcher =
        caller.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            onResult?.invoke(granted)
            onResult = null
        }

    fun request(
        onResult: (Boolean) -> Unit
    ) {
        this.onResult = onResult
        launcher.launch(Manifest.permission.CAMERA)
    }
}
