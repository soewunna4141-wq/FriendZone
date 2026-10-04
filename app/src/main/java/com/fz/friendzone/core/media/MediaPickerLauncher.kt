package com.fz.friendzone.core.media

import android.net.Uri

interface MediaPickerLauncher {

    fun pickImage(onResult: (Uri?) -> Unit)

    fun pickVideo(onResult: (Uri?) -> Unit)
}
