package az.less.mobile.utils

import java.util.Locale

actual fun deviceLanguageCode(): String = Locale.getDefault().language
