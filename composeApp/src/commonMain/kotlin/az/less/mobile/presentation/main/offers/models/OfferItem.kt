package az.less.mobile.presentation.main.offers.models

data class OfferItem(
    val id: String,
    val title: String,
    val imageUrl: String? = null,
    val imageBgColor: String = "#fff2eb", // Background color for image area
    val originalPrice: String, // e.g. "12.99"
    val currentPrice: String, // e.g. "12.99"
    val restaurantName: String, // e.g. "Small Surprise Bag"
    val restaurantLogoUrl: String? = null,
    val pickupTime: String, // e.g. "Pick up from 17:00 to 23:00"
    val rating: Float = 0f,
    val distance: String // e.g. "1.2 km"
)

