package az.less.mobile.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSString
import platform.Foundation.create
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController

/**
 * iOS implementation of share functionality
 * Uses UIActivityViewController to share content
 */
@OptIn(ExperimentalForeignApi::class)
actual fun shareContent(content: String) {
    val activityViewController = UIActivityViewController(
        activityItems = listOf(content),
        applicationActivities = null
    )

    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    rootViewController?.presentViewController(activityViewController, animated = true, completion = null)
}

