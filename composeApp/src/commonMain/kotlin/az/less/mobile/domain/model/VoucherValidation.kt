package az.less.mobile.domain.model

/**
 * Result of voucher validation against an order.
 */
sealed interface VoucherValidation {
    /** Voucher can be applied. [effectiveDiscount] is the actual discount amount (capped by maxDiscount). */
    data class Valid(val effectiveDiscount: Double) : VoucherValidation

    /** Voucher cannot be applied. [reason] explains why. */
    data class Invalid(val reason: String) : VoucherValidation
}
