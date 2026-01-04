package az.less.mobile.presentation.client.reserve.models

/**
 * Model representing a voucher that can be applied to an order
 */
data class Voucher(
    val id: String,
    val name: String,
    val discountAmount: Double,
    val expiresInDays: Int,
    val isSelected: Boolean = false
)
