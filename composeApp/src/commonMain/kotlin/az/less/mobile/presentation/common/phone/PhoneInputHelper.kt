package az.less.mobile.presentation.common.phone

/**
 * Result of sanitizing a raw text-field input for a multi-country phone field.
 */
data class PhoneSanitizeResult(
    val country: PhoneCountry,
    val localDigits: String
)

/**
 * Sanitize free-form input into a (country, localDigits) pair.
 *
 * Behaviour:
 *  - Strips everything but digits.
 *  - If the digit prefix matches a known [PhoneCountry] dial code (longest
 *    match wins) and there are more digits after the prefix, the country is
 *    updated and the remaining digits are kept as local.
 *  - Otherwise the [currentCountry] is kept and the digits are treated as
 *    local digits.
 *  - Local digits are always capped at the country's [PhoneCountry.localDigits]
 *    length to avoid overflowing the visual mask (which causes
 *    `OffsetMapping` crashes in Compose's `BasicTextField`).
 */
fun sanitizePhoneInput(input: String, currentCountry: PhoneCountry): PhoneSanitizeResult {
    val digits = input.filter(Char::isDigit)
    val detected = PhoneCountry.detect(digits)
    return if (detected != null) {
        val local = digits.substring(detected.dialCode.length).take(detected.localDigits)
        PhoneSanitizeResult(detected, local)
    } else {
        PhoneSanitizeResult(currentCountry, digits.take(currentCountry.localDigits))
    }
}
