package az.less.mobile.presentation.common.phone

/**
 * Supported phone countries with input mask and dial code.
 *
 * The [mask] uses the inputmask-kmp `+{dialCode} ...` autocomplete syntax so
 * that pasting a number with or without the leading country code is handled
 * uniformly. The state stores only the local digits (no dial code prefix).
 */
enum class PhoneCountry(
    val dialCode: String,
    val mask: String,
    val displayName: String,
    val flagEmoji: String,
    val localDigits: Int
) {
    AZERBAIJAN("994", "+{994} [00] [000] [00] [00]", "Azerbaijan", "🇦🇿", 9),
    RUSSIA("7", "+{7} [000] [000] [00] [00]", "Russia", "🇷🇺", 10),
    UKRAINE("380", "+{380} [00] [000] [00] [00]", "Ukraine", "🇺🇦", 9),
    BELARUS("375", "+{375} [00] [000] [00] [00]", "Belarus", "🇧🇾", 9),
    UZBEKISTAN("998", "+{998} [00] [000] [00] [00]", "Uzbekistan", "🇺🇿", 9),
    KYRGYZSTAN("996", "+{996} [000] [000] [000]", "Kyrgyzstan", "🇰🇬", 9),
    GEORGIA("995", "+{995} [000] [00] [00] [00]", "Georgia", "🇬🇪", 9),
    ARMENIA("374", "+{374} [00] [000] [000]", "Armenia", "🇦🇲", 8),
    TURKEY("90", "+{90} [000] [000] [00] [00]", "Türkiye", "🇹🇷", 10),
    POLAND("48", "+{48} [000] [000] [000]", "Poland", "🇵🇱", 9),
    GERMANY("49", "+{49} [000] [00000000]", "Germany", "🇩🇪", 11),
    LITHUANIA("370", "+{370} [000] [00] [000]", "Lithuania", "🇱🇹", 8),
    LATVIA("371", "+{371} [00] [000] [000]", "Latvia", "🇱🇻", 8),
    ESTONIA("372", "+{372} [0000] [0000]", "Estonia", "🇪🇪", 8);

    companion object {
        val Default = AZERBAIJAN

        /**
         * Detect the country whose dial code is the longest prefix match of the
         * digit-only representation of [input]. Returns null if nothing matches
         * or if the input contains only the dial code with no local digits.
         */
        fun detect(input: String): PhoneCountry? {
            val digits = input.filter(Char::isDigit)
            return entries
                .filter { digits.startsWith(it.dialCode) && digits.length > it.dialCode.length }
                .maxByOrNull { it.dialCode.length }
        }

        /**
         * Parse a phone number (with or without `+` prefix) into the country
         * and the local digits. Falls back to [Default] when no country
         * dial code matches.
         */
        fun parse(phone: String): Pair<PhoneCountry, String> {
            val digits = phone.filter(Char::isDigit)
            if (digits.isEmpty()) return Default to ""
            val country = entries
                .filter { digits.startsWith(it.dialCode) }
                .maxByOrNull { it.dialCode.length }
            return if (country != null) {
                country to digits.substring(country.dialCode.length).take(country.localDigits)
            } else {
                Default to digits.take(Default.localDigits)
            }
        }
    }
}
