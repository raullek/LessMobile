package az.less.mobile.utils

/**
 * Extracts HH:mm from an ISO 8601 datetime string.
 * E.g. "2026-03-04T16:45:00.000Z" → "16:45"
 *      "17:00" → "17:00"
 * Returns null if the string is blank or unparseable.
 */
fun String.extractTime(): String? {
    if (isBlank()) return null
    return try {
        val timeIndex = indexOf('T')
        val timePart = if (timeIndex >= 0) substring(timeIndex + 1) else this
        if (timePart.length < 5) null else timePart.take(5)
    } catch (_: Exception) {
        null
    }
}

/**
 * Extracts the date part (YYYY-MM-DD) from an ISO 8601 datetime string.
 * E.g. "2026-03-04T16:45:00.000Z" → "2026-03-04"
 * Returns null if the string is blank or unparseable.
 */
fun String.extractDate(): String? {
    if (isBlank()) return null
    return try {
        val timeIndex = indexOf('T')
        if (timeIndex >= 10) substring(0, timeIndex) else take(10).ifEmpty { null }
    } catch (_: Exception) {
        null
    }
}

/**
 * Formats a pickup time range from two ISO 8601 strings.
 * Returns a Pair of (startTime, endTime) as "HH:mm" strings,
 * or null if either is null or unparseable.
 */
fun formatPickupTimePair(start: String?, end: String?): Pair<String, String>? {
    if (start == null || end == null) return null
    val startTime = start.extractTime() ?: return null
    val endTime = end.extractTime() ?: return null
    return Pair(startTime, endTime)
}
