package com.fz.friendzone.core.media

import android.net.Uri

interface CameraCaptureManager {

    fun captureImage(
        onResult: (Uri?) -> Unit
    )

    fun startVideoCapture(
        onResult: (Uri?) -> Unit
    )

    fun stopVideoCapture()
}
