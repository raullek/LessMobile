package az.less.mobile.domain.model

/**
 * Result of order price calculation.
 * All amounts are in currency units (e.g. AZN), safe from floating-point drift.
 */
data class OrderPriceResult(
    val boxTotal: Double,
    val discount: Double,
    val serviceFee: Double,
    val subtotal: Double
)
