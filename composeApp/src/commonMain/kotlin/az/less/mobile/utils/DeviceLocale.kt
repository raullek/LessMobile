package az.less.mobile.utils

private val SUPPORTED_LOCALES = setOf("az", "en", "ru")
private const val DEFAULT_LOCALE = "az"

expect fun deviceLanguageCode(): String

fun deviceLocaleHeader(): String {
    val code = deviceLanguageCode().lowercase()
    return if (code in SUPPORTED_LOCALES) code else DEFAULT_LOCALE
}
