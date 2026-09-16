package com.fz.friendzone

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * Which platforms the user can target from the composer screen.
 */
enum class Platform(val label: String, val packageName: String) {
    FACEBOOK("Facebook", "com.facebook.katana"),
    TIKTOK("TikTok", "com.zhiliaoapp.musically"),
    TELEGRAM("Telegram", "org.telegram.messenger")
}

/**
 * A single post the user is composing: caption text plus an optional
 * local media file (image or video) picked from the device.
 */
data class PostContent(
    val caption: String,
    val mediaUri: Uri? = null,
    val mediaMimeType: String? = null
)

/**
 * Builds and launches the native Android Share Sheet, pre-targeted at one
 * platform at a time. This does NOT auto-post — it hands off to the
 * platform's own app so the user makes the final "post" tap themselves.
 * That keeps us clear of each platform's API-approval requirements.
 */
object ShareDispatcher {

    /**
     * Opens the share flow for a single platform.
     *
     * Behaviour:
     *  - If the platform's app is installed, the Intent is targeted directly
     *    at its package, so Android opens that app's own share/compose screen.
     *  - If it's not installed, we fall back to the system chooser so the
     *    user isn't stuck.
     *  - Text-only posts work for Facebook/Telegram. TikTok's share target
     *    generally requires an image or video attachment, so we warn the
     *    user if they try to send text-only content to TikTok.
     */
    fun share(context: Context, platform: Platform, content: PostContent) {
        if (platform == Platform.TIKTOK && content.mediaUri == null) {
            Toast.makeText(
                context,
                "TikTok requires a photo or video — add media before sharing to TikTok.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val intent = buildIntent(content)
        val isInstalled = isPackageInstalled(context, platform.packageName)

        if (isInstalled) {
            intent.setPackage(platform.packageName)
            try {
                context.startActivity(intent)
                return
            } catch (e: Exception) {
                // Some apps restrict which mime types / actions they accept
                // directly. Fall back to the generic chooser below.
            }
        }

        // Fallback: generic chooser (also used when the app isn't installed,
        // so the user can pick whatever's available, e.g. Telegram web).
        val chooserTitle = if (isInstalled) {
            "Share to ${platform.label}"
        } else {
            "${platform.label} not found — choose an app"
        }
        context.startActivity(Intent.createChooser(intent, chooserTitle))
    }

    /**
     * Fires the share intent for every platform the user selected, one
     * after another. Each opens as its own share sheet / target app; the
     * user confirms each post individually.
     */
    fun shareToAll(context: Context, platforms: List<Platform>, content: PostContent) {
        platforms.forEach { platform ->
            share(context, platform, content)
        }
    }

    private fun buildIntent(content: PostContent): Intent {
        return if (content.mediaUri != null) {
            Intent(Intent.ACTION_SEND).apply {
                type = content.mediaMimeType ?: "image/*"
                putExtra(Intent.EXTRA_STREAM, content.mediaUri)
                putExtra(Intent.EXTRA_TEXT, content.caption)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        } else {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, content.caption)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }

    private fun isPackageInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: Exception) {
            false
        }
    }
}
