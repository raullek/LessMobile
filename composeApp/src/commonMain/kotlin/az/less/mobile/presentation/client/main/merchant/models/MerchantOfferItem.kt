package az.less.mobile.presentation.client.main.merchant.models

/**
 * Merchant offer item with additional badge info
 */
data class MerchantOfferItem(
    val id: String,
    val imageUrl: String? = null,
    val merchantName: String,
    val merchantLogoUrl: String? = null,
    val pickupTime: String,
    val rating: Float,
    val distance: String,
    val itemsOnSale: Int = 0, // 0 means "No active discount"
    val hasActiveDiscount: Boolean = true
)
