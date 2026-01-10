package az.less.mobile.utils

import android.content.Intent
import az.less.mobile.preferences.appContext

/**
 * Android implementation of share functionality
 * Uses Android Intent.ACTION_SEND to share content
 */
actual fun shareContent(content: String) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(intent, null)
    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    appContext.startActivity(shareIntent)
}

