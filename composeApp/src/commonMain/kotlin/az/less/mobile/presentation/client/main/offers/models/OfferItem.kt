package az.less.mobile.presentation.client.main.offers.models

/**
 * Merchant info nested in offer
 */
data class OfferMerchant(
    val id: String,
    val name: String,
    val logoUrl: String? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val rating: Float
)

/**
 * Offer item model
 */
data class OfferItem(
    val id: String,
    val title: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val imageBgColor: String = "#fff2eb",
    val quantity: Int = 0,
    val originalPrice: String,
    val currentPrice: String,
    val bagType: String? = null,
    val category: String,
    val pickupTime: String,
    val merchant: OfferMerchant
) {
    // Backward compatibility properties
    val restaurantName: String get() = merchant.name
    val restaurantLogoUrl: String? get() = merchant.logoUrl
    val distance: String get() = ""
    val rating: Float get() = merchant.rating
    val itemsLeft: Int get() = quantity
}
