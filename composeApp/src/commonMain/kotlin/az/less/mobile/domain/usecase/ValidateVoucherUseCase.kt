package az.less.mobile.domain.usecase

import az.less.mobile.domain.model.VoucherValidation
import az.less.mobile.domain.usecase.CalculateOrderPriceUseCase.Companion.effectiveDiscountCents
import az.less.mobile.domain.usecase.CalculateOrderPriceUseCase.Companion.fromCents
import az.less.mobile.domain.usecase.CalculateOrderPriceUseCase.Companion.toCents
import az.less.mobile.presentation.client.reserve.models.Voucher
import az.less.mobile.utils.formatPrice

/**
 * Validates whether a voucher can be applied to an order.
 *
 * Rules:
 * 1. If voucher has minSubtotal — boxTotal must be ≥ minSubtotal
 * 2. boxTotal - effectiveDiscount must be ≥ MIN_ORDER_AMOUNT (order can't be zeroed out)
 */
class ValidateVoucherUseCase {

    operator fun invoke(
        voucher: Voucher,
        quantity: Int,
        pricePerPiece: Double
    ): VoucherValidation {
        val boxTotalCents = toCents(pricePerPiece) * quantity

        // Check minSubtotal
        val minSubtotal = voucher.minSubtotal
        if (minSubtotal != null && boxTotalCents < toCents(minSubtotal)) {
            return VoucherValidation.Invalid(
                "Minimum order amount for this voucher is ${minSubtotal.formatPrice()} ₼"
            )
        }

        // Check that order won't be zeroed out
        val discountCents = effectiveDiscountCents(voucher)
        val minCents = toCents(MIN_ORDER_AMOUNT)
        if ((boxTotalCents - discountCents) < minCents) {
            return VoucherValidation.Invalid("Voucher discount exceeds order amount")
        }

        return VoucherValidation.Valid(
            effectiveDiscount = fromCents(discountCents)
        )
    }

    companion object {
        const val MIN_ORDER_AMOUNT = 0.10
    }
}
