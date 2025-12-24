package az.less.mobile.presentation.merchant.orders.model

/**
 * Represents an order item for the merchant orders screen
 */
data class MerchOrderItem(
    val id: String,
    val imageUrl: String? = null,
    val merchantLogoUrl: String? = null,
    val merchantName: String,
    val productName: String,
    val originalPrice: String,
    val discountedPrice: String,
    val pickupTimeStart: String,
    val pickupTimeEnd: String,
    val orderNumber: String,
    val rating: String,
    val distance: String,
    val itemsLeft: Int,
    val buttonState: OrderButtonState
)

/**
 * Represents the different states of the order action button
 * 
 * Translations:
 * - "Təhvil verildi" (Azerbaijani) = "Handed Over"
 * - "Отменить лот" (Russian) = "Cancel Lot"
 * - "Время для отмены завершилось" (Russian) = "Cancellation Time Ended"
 */
enum class OrderButtonState {
    /** Order can be marked as handed over to customer */
    HANDED_OVER,
    /** Order can be cancelled by merchant */
    CANCEL_LOT,
    /** Cancellation window has expired */
    CANCELLATION_TIME_ENDED
}

/**
 * Order tab for filtering orders
 */
enum class MerchOrderTab {
    /** Orders awaiting pickup by customer */
    AWAITING_PICKUP,
    /** Orders awaiting purchase by customer */
    AWAITING_PURCHASE
}

