package az.less.mobile.utils

/**
 * Gated input filter for currency / quantity text fields.
 *
 * Use it inside `onValueChange` to reject keystrokes that would put the field
 * in an invalid state, instead of accepting any string and validating later:
 *
 * ```
 * DsTextField(
 *     value = state.price,
 *     onValueChange = { raw ->
 *         PriceInputFilter.sanitizeCurrency(raw)?.let { onIntent(...(it)) }
 *     }
 * )
 * ```
 *
 * Reject (returns `null`) means "drop the keystroke, keep the previous value".
 * Accept returns the canonical string the field should display.
 *
 * Why we filter on input rather than only on submit:
 * - Forms feel responsive — the field literally can't show garbage.
 * - We don't have to localise / show inline errors for "0000.123 is invalid".
 * - The viewmodel always sees a parseable value, so submit-time logic stays
 *   simple (parse to Double / BigDecimal once, no re-validation).
 */
object PriceInputFilter {

    /** Maximum digits before the decimal separator. 7 → up to 9,999,999.99. */
    private const val MAX_INTEGER_DIGITS = 7

    /** Maximum digits after the decimal separator (cents). */
    private const val MAX_FRACTION_DIGITS = 2

    /**
     * Sanitises [input] for a currency field.
     *
     * Allowed:
     * - empty string (so the user can clear the field);
     * - integer part of 1..[MAX_INTEGER_DIGITS] digits with no leading zeros,
     *   except a lone `"0"`;
     * - optional single `.` separator;
     * - 0..[MAX_FRACTION_DIGITS] digits after the separator.
     *
     * Returns `null` to reject the keystroke. Treats `,` as `.` so locales
     * that use a decimal comma still produce a parseable value.
     */
    fun sanitizeCurrency(input: String): String? {
        if (input.isEmpty()) return ""

        val normalized = input.replace(',', '.')

        // Reject any character that is neither a digit nor the separator.
        if (normalized.any { !it.isDigit() && it != '.' }) return null

        val dotCount = normalized.count { it == '.' }
        if (dotCount > 1) return null

        val (intPart, fracPart) = if (dotCount == 1) {
            val idx = normalized.indexOf('.')
            normalized.substring(0, idx) to normalized.substring(idx + 1)
        } else {
            normalized to ""
        }

        // Allow a leading "." by treating it as "0.<frac>".
        val effectiveInt = intPart.ifEmpty { "0" }

        if (effectiveInt.length > MAX_INTEGER_DIGITS) return null
        // No leading zeros: "0" is fine, "01"/"007" is not.
        if (effectiveInt.length > 1 && effectiveInt.startsWith("0")) return null
        if (fracPart.length > MAX_FRACTION_DIGITS) return null

        return if (dotCount == 1) "$effectiveInt.$fracPart" else effectiveInt
    }

    /**
     * Sanitises [input] for an integer quantity field. Same rules as
     * [sanitizeCurrency] minus the decimal separator.
     */
    fun sanitizeInteger(input: String, maxDigits: Int = MAX_INTEGER_DIGITS): String? {
        if (input.isEmpty()) return ""
        if (!input.all { it.isDigit() }) return null
        if (input.length > maxDigits) return null
        if (input.length > 1 && input.startsWith("0")) return null
        return input
    }
}
