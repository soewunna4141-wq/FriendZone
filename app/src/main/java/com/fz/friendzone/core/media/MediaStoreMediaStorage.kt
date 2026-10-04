package com.fz.friendzone.core.media

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore

class MediaStoreMediaStorage(
    private val contentResolver: ContentResolver
) : MediaStorage {

    override fun save(
        source: Uri,
        mediaId: String,
        onResult: (Uri?) -> Unit
    ) {
        val mimeType =
            contentResolver.getType(source)
                ?: "application/octet-stream"

        val isVideo = mimeType.startsWith("video/")
        val collection =
            if (isVideo) {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

        val extension =
            mimeType.substringAfter(
                "/",
                "bin"
            )

        val fileName =
            "FriendZone_$mediaId.$extension"

        val values =
            ContentValues().apply {
                put(
                    MediaStore.MediaColumns.DISPLAY_NAME,
                    fileName
                )
                put(
                    MediaStore.MediaColumns.MIME_TYPE,
                    mimeType
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        if (isVideo) {
                            Environment.DIRECTORY_MOVIES +
                                "/FriendZone"
                        } else {
                            Environment.DIRECTORY_PICTURES +
                                "/FriendZone"
                        }
                    )

                    put(
                        MediaStore.MediaColumns.IS_PENDING,
                        1
                    )
                }
            }

        val destination =
            contentResolver.insert(
                collection,
                values
            )

        if (destination == null) {
            onResult(null)
            return
        }

        try {
            contentResolver.openInputStream(source).use { input ->
                contentResolver.openOutputStream(destination).use { output ->
                    if (input == null || output == null) {
                        throw IllegalStateException(
                            "Unable to open media streams"
                        )
                    }

                    input.copyTo(output)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val pendingValues =
                    ContentValues().apply {
                        put(
                            MediaStore.MediaColumns.IS_PENDING,
                            0
                        )
                    }

                contentResolver.update(
                    destination,
                    pendingValues,
                    null,
                    null
                )
            }

            onResult(destination)
        } catch (throwable: Throwable) {
            contentResolver.delete(
                destination,
                null,
                null
            )

            onResult(null)
        }
    }

    override fun get(
        mediaId: String
    ): Uri? {
        return findMediaUri(
            mediaId = mediaId,
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ) ?: findMediaUri(
            mediaId = mediaId,
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )
    }

    override fun delete(
        mediaId: String
    ): Boolean {
        val uri =
            get(mediaId) ?: return false

        return contentResolver.delete(
            uri,
            null,
            null
        ) > 0
    }

    private fun findMediaUri(
        mediaId: String,
        collection: Uri
    ): Uri? {
        val fileName =
            "FriendZone_$mediaId"

        val projection =
            arrayOf(
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DISPLAY_NAME
            )

        contentResolver.query(
            collection,
            projection,
            "${MediaStore.MediaColumns.DISPLAY_NAME} LIKE ?",
            arrayOf("$fileName.%"),
            null
        )?.use { cursor ->

            val idColumn =
                cursor.getColumnIndexOrThrow(
                    MediaStore.MediaColumns._ID
                )

            if (cursor.moveToFirst()) {
                val id =
                    cursor.getLong(idColumn)

                return Uri.withAppendedPath(
                    collection,
                    id.toString()
                )
            }
        }

        return null
    }
}
