package az.less.mobile.presentation.client.account.account

enum class Gender(val displayName: String, val apiValue: String) {
    Male("Male", "male"),
    Female("Female", "female"),
    Other("Don't want to specify", "other");

    companion object {
        fun fromApiValue(value: String?): Gender? {
            return entries.find { it.apiValue == value }
        }
    }
}

/**
 * Converts ISO date "1998-02-14" to raw digits "14021998" for masked display.
 */
fun String.isoDateToDisplay(): String {
    val parts = split("-")
    if (parts.size != 3) return ""
    return "${parts[2]}${parts[1]}${parts[0]}"
}

/**
 * Converts raw digits "14021998" to ISO format "1998-02-14T00:00:00.000Z" for API.
 */
fun String.displayDateToIso(): String? {
    if (length != 8) return null
    val day = substring(0, 2)
    val month = substring(2, 4)
    val year = substring(4, 8)
    return "$year-$month-${day}T00:00:00.000Z"
}
