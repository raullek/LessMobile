package az.less.mobile.presentation.client.reserve.models

/**
 * Model representing a voucher that can be applied to an order
 *
 * @param discountAmount fixed discount value in currency
 * @param minSubtotal minimum order subtotal required to apply this voucher (null = no minimum)
 * @param maxDiscount maximum discount amount allowed (null = no cap, use full discountAmount)
 */
data class Voucher(
    val id: String,
    val name: String,
    val discountAmount: Double,
    val minSubtotal: Double? = null,
    val maxDiscount: Double? = null,
    val expiresInDays: Int,
    val isSelected: Boolean = false
)
