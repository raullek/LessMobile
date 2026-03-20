package az.less.mobile.presentation.merchant.orders.model

/**
 * Bought box (awaiting pickup) - order placed by a client
 */
data class BoughtBoxItem(
    val id: String,
    val orderId: String,
    val reserveNumber: String,
    val boxTitle: String,
    val imageUrl: String? = null,
    val originalPrice: Double,
    val discountedPrice: Double,
    val clientName: String,
    val clientAvatar: String? = null,
    val quantity: Int,
    val subtotal: Double,
    val status: String,
    val pickupTimeFormatted: String,
    val canDeliver: Boolean = false
)

/**
 * Created box (awaiting purchase) - lot posted by the merchant
 */
data class CreatedBoxItem(
    val id: String,
    val title: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val originalPrice: Double,
    val discountedPrice: Double,
    val quantity: Int,
    val soldCount: Int,
    val availableItems: Int,
    val pickupTimeFormatted: String,
    val status: String? = null,
    val isActive: Boolean = true,
    val canCancel: Boolean = false,
    val timeRemaining: String? = null,
    val timeRemainingSeconds: Int = 0,
    val cancellationExpired: Boolean = false,
    val closeTimeFormatted: String? = null,
    val cancellationMessage: String? = null,
    val cancelButtonText: String? = null
) {
    val isCancelEnabled: Boolean
        get() = canCancel && !cancellationExpired && timeRemainingSeconds > 0
}

/**
 * Order tab for filtering orders
 */
enum class MerchOrderTab {
    AWAITING_PICKUP,
    AWAITING_PURCHASE
}
