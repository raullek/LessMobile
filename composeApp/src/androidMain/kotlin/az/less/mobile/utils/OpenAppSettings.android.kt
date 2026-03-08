package az.less.mobile.utils

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import az.less.mobile.preferences.appContext

actual fun openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:${appContext.packageName}")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    appContext.startActivity(intent)
}
