package az.less.mobile.utils

// ── ISO 8601 parsing helpers ─────────────────────────────────────────

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

// ── ISO ↔ display date conversions ───────────────────────────────────

private const val ISO_DATE_SUFFIX = "T00:00:00.000Z"

/**
 * Converts an ISO date string to display format.
 * "1998-02-14" or "1998-02-14T00:00:00.000Z" → "14.02.1998"
 */
fun String.isoDateToDisplayDate(): String? {
    val datePart = extractDate() ?: return null
    val parts = datePart.split("-")
    if (parts.size != 3) return null
    return "${parts[2]}.${parts[1]}.${parts[0]}"
}

/**
 * Converts a display date string to full ISO 8601 format.
 * "14.02.1998" → "1998-02-14T00:00:00.000Z"
 */
fun String.displayDateToIsoDateTime(): String? {
    val parts = split(".")
    if (parts.size != 3) return null
    val day = parts[0]
    val month = parts[1]
    val year = parts[2]
    if (day.length != 2 || month.length != 2 || year.length != 4) return null
    return "$year-$month-$day$ISO_DATE_SUFFIX"
}

/**
 * Converts a display date string to ISO date only.
 * "14.02.1998" → "1998-02-14"
 */
fun String.displayDateToIsoDate(): String? {
    val parts = split(".")
    if (parts.size != 3) return null
    val day = parts[0]
    val month = parts[1]
    val year = parts[2]
    if (day.length != 2 || month.length != 2 || year.length != 4) return null
    return "$year-$month-$day"
}

// ── Epoch millis ↔ display date conversions ──────────────────────────

private const val MILLIS_PER_DAY = 86_400_000L

private fun isLeapYear(year: Int): Boolean =
    (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

private fun daysInMonth(month: Int, year: Int): Int = when (month) {
    1 -> 31; 2 -> if (isLeapYear(year)) 29 else 28; 3 -> 31
    4 -> 30; 5 -> 31; 6 -> 30; 7 -> 31; 8 -> 31
    9 -> 30; 10 -> 31; 11 -> 30; 12 -> 31
    else -> 30
}

/**
 * Converts UTC epoch millis to display date format.
 * 887328000000 → "14.02.1998"
 */
fun Long.millisToDisplayDate(): String {
    val (y, m, d) = millisToYmd()
    return "${d.toString().padStart(2, '0')}.${m.toString().padStart(2, '0')}.$y"
}

/**
 * Converts UTC epoch millis to API date format (YYYY-MM-DD).
 * 887328000000 → "1998-02-14"
 */
fun Long.millisToApiDate(): String {
    val (y, m, d) = millisToYmd()
    return "$y-${m.toString().padStart(2, '0')}-${d.toString().padStart(2, '0')}"
}

private fun Long.millisToYmd(): Triple<Int, Int, Int> {
    var days = (this / MILLIS_PER_DAY).toInt()
    var year = 1970
    while (true) {
        val daysInYear = if (isLeapYear(year)) 366 else 365
        if (days < daysInYear) break
        days -= daysInYear
        year++
    }
    var month = 1
    while (true) {
        val dim = daysInMonth(month, year)
        if (days < dim) break
        days -= dim
        month++
    }
    return Triple(year, month, days + 1)
}

/**
 * Converts a display date string to UTC epoch millis.
 * "14.02.1998" → 887328000000
 */
fun String.displayDateToMillis(): Long? {
    val parts = split(".")
    if (parts.size != 3) return null
    return ymdToMillis(parts[2].toIntOrNull(), parts[1].toIntOrNull(), parts[0].toIntOrNull())
}

/**
 * Converts an API date string (YYYY-MM-DD) to UTC epoch millis.
 * "1998-02-14" → 887328000000
 */
fun String.apiDateToMillis(): Long? {
    val parts = split("-")
    if (parts.size != 3) return null
    return ymdToMillis(parts[0].toIntOrNull(), parts[1].toIntOrNull(), parts[2].toIntOrNull())
}

private fun ymdToMillis(year: Int?, month: Int?, day: Int?): Long? {
    if (year == null || month == null || day == null) return null
    return try {
        var totalDays = 0L
        for (y in 1970 until year) {
            totalDays += if (isLeapYear(y)) 366 else 365
        }
        for (m in 1 until month) {
            totalDays += daysInMonth(m, year)
        }
        totalDays += (day - 1)
        totalDays * MILLIS_PER_DAY
    } catch (_: Exception) {
        null
    }
}

/**
 * Converts UTC epoch millis to full ISO 8601 datetime string.
 * 887328000000 → "1998-02-14T00:00:00.000Z"
 */
fun Long.millisToIsoDateTime(): String {
    val display = millisToDisplayDate()
    return display.displayDateToIsoDateTime() ?: ""
}
