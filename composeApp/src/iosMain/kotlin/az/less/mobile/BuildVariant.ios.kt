package az.less.mobile

import platform.Foundation.NSBundle

actual fun currentBuildVariant(): BuildVariant {
    val variant = NSBundle.mainBundle.objectForInfoDictionaryKey("Variant") as? String
    return when (variant) {
        "prod" -> BuildVariant.PRODUCTION
        else -> BuildVariant.DEVELOPMENT
    }
}
