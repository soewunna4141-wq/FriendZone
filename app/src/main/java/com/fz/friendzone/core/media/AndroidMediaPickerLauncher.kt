package com.fz.friendzone.core.media

import android.net.Uri
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts

class AndroidMediaPickerLauncher(
    caller: ActivityResultCaller
) : MediaPickerLauncher {

    private var imageCallback: ((Uri?) -> Unit)? = null
    private var videoCallback: ((Uri?) -> Unit)? = null

    private val imagePicker =
        caller.registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            imageCallback?.invoke(uri)
            imageCallback = null
        }

    private val videoPicker =
        caller.registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            videoCallback?.invoke(uri)
            videoCallback = null
        }

    override fun pickImage(onResult: (Uri?) -> Unit) {
        imageCallback = onResult
        imagePicker.launch(
            ActivityResultContracts.PickVisualMedia.ImageOnly
        )
    }

    override fun pickVideo(onResult: (Uri?) -> Unit) {
        videoCallback = onResult
        videoPicker.launch(
            ActivityResultContracts.PickVisualMedia.VideoOnly
        )
    }
}
