package az.less.mobile.utils

import android.content.Intent
import android.net.Uri
import az.less.mobile.preferences.appContext

/**
 * Android: launch a `geo:` URI via `ACTION_VIEW`, wrapped in `createChooser`
 * so every tap re-presents the chooser. This guarantees Waze users (and
 * OsmAnd / Yandex / 2GIS users) see their preferred app rather than getting
 * locked into whichever app the system remembered as default.
 */
actual fun openDirections(latitude: Double, longitude: Double, label: String) {
    val encodedLabel = Uri.encode(label.ifBlank { "$latitude,$longitude" })
    val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($encodedLabel)")
    val viewIntent = Intent(Intent.ACTION_VIEW, uri)
    val chooser = Intent.createChooser(viewIntent, null).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    appContext.startActivity(chooser)
}
