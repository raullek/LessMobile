package az.less.mobile.utils

import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLQueryItem
import platform.UIKit.UIApplication

/**
 * iOS: try Google Maps, then Waze, then fall back to Apple Maps. Apple Maps
 * uses the universal HTTPS URL (no browser bounce). Each branch sets the
 * destination in *directions* mode (`directionsmode=driving` / `navigate=yes` /
 * `dirflg=d`).
 *
 * The Apple Maps URL is built via [NSURLComponents] so the label is properly
 * percent-encoded by Foundation — including non-ASCII characters (Azerbaijani,
 * Russian, Cyrillic, etc.).
 *
 * NOTE: For `canOpenURL` to return true for the `comgooglemaps` and `waze`
 * schemes on iOS 9+, both must be declared in `Info.plist` under
 * `LSApplicationQueriesSchemes`.
 */
actual fun openDirections(latitude: Double, longitude: Double, label: String) {
    val app = UIApplication.sharedApplication

    val googleMapsUrl = NSURL.URLWithString(
        "comgooglemaps://?daddr=$latitude,$longitude&directionsmode=driving"
    )
    if (googleMapsUrl != null && app.canOpenURL(googleMapsUrl)) {
        app.openURL(googleMapsUrl, options = emptyMap<Any?, Any>(), completionHandler = null)
        return
    }

    val wazeUrl = NSURL.URLWithString(
        "waze://?ll=$latitude,$longitude&navigate=yes"
    )
    if (wazeUrl != null && app.canOpenURL(wazeUrl)) {
        app.openURL(wazeUrl, options = emptyMap<Any?, Any>(), completionHandler = null)
        return
    }

    val components = NSURLComponents().apply {
        scheme = "https"
        host = "maps.apple.com"
        queryItems = listOf(
            NSURLQueryItem.queryItemWithName("daddr", "$latitude,$longitude"),
            NSURLQueryItem.queryItemWithName("dirflg", "d"),
            NSURLQueryItem.queryItemWithName("q", label.ifBlank { "$latitude,$longitude" })
        )
    }
    val appleUrl = components.URL ?: return
    app.openURL(appleUrl, options = emptyMap<Any?, Any>(), completionHandler = null)
}
