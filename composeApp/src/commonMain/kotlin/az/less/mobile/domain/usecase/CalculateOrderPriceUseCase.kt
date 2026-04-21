package az.less.mobile.domain.usecase

import az.less.mobile.domain.model.OrderPriceResult
import az.less.mobile.presentation.client.reserve.models.Voucher
import kotlin.math.roundToLong

/**
 * Calculates order pricing with safe integer arithmetic (cents).
 *
 * Formula:
 *   boxTotal       = pricePerPiece × quantity
 *   effectiveDiscount = min(voucherAmount, maxDiscount ?: voucherAmount)
 *   afterDiscount  = boxTotal - effectiveDiscount  (≥ 0)
 *   serviceFee     = afterDiscount × serviceFeeRate
 *   subtotal       = afterDiscount + serviceFee
 */
class CalculateOrderPriceUseCase {

    operator fun invoke(
        quantity: Int,
        pricePerPiece: Double,
        voucher: Voucher?,
        serviceFeeRate: Double
    ): OrderPriceResult {
        val boxTotalCents = toCents(pricePerPiece) * quantity
        val discountCents = voucher?.let { effectiveDiscountCents(it) } ?: 0L
        val afterDiscountCents = (boxTotalCents - discountCents).coerceAtLeast(0)
        val serviceFeeCents = (afterDiscountCents * serviceFeeRate).roundToLong().coerceAtLeast(0)
        val subtotalCents = afterDiscountCents + serviceFeeCents

        return OrderPriceResult(
            boxTotal = fromCents(boxTotalCents),
            discount = fromCents(discountCents),
            serviceFee = fromCents(serviceFeeCents),
            subtotal = fromCents(subtotalCents)
        )
    }

    companion object {
        internal fun toCents(amount: Double): Long = (amount * 100).roundToLong()
        internal fun fromCents(cents: Long): Double = cents / 100.0

        internal fun effectiveDiscountCents(voucher: Voucher): Long {
            val raw = toCents(voucher.discountAmount)
            val maxCap = voucher.maxDiscount?.let { toCents(it) }
            return if (maxCap != null) raw.coerceAtMost(maxCap) else raw
        }
    }
}
