package az.less.mobile.utils

import kotlin.math.abs
import kotlin.math.round
import kotlin.math.roundToLong

/**
 * Format Double to 2 decimal places for price display.
 * Uses integer math to avoid floating-point representation issues.
 * E.g. 12.5 → "12.50", 37.650000000000006 → "37.65"
 */
fun Double.formatPrice(): String {
    val cents = round(this * 100).toLong()
    val whole = cents / 100
    val frac = abs(cents % 100)
    return "$whole.${frac.toString().padStart(2, '0')}"
}

/**
 * Round to 2 decimal places using integer math to avoid floating-point drift.
 * E.g. 3 * 12.55 = 37.650000000000006 → 37.65
 */
fun Double.roundPrice(): Double {
    return (this * 100).roundToLong() / 100.0
}

/**
 * Format Double to 1 decimal place. KMP-safe (no String.format).
 * E.g. 4.7321 → "4.7", 3.0 → "3.0"
 */
fun Double.formatOneDecimal(): String {
    val rounded = round(this * 10).toLong()
    val whole = rounded / 10
    val frac = abs(rounded % 10)
    return "$whole.$frac"
}

fun Float.formatOneDecimal(): String = this.toDouble().formatOneDecimal()
