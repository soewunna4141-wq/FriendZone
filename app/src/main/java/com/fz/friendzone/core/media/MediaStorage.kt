package com.fz.friendzone.core.media

import android.net.Uri

interface MediaStorage {

    fun save(
        source: Uri,
        mediaId: String,
        onResult: (Uri?) -> Unit
    )

    fun get(
        mediaId: String
    ): Uri?

    fun delete(
        mediaId: String
    ): Boolean
}
