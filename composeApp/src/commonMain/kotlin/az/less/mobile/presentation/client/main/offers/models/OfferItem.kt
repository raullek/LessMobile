package az.less.mobile.presentation.client.main.offers.models

data class OfferItem(
    val id: String,
    val title: String, // e.g. "Belgian Chocolate & Coffee"
    val imageUrl: String? = null,
    val imageBgColor: String = "#fff2eb", // Background color for image area
    val originalPrice: String, // e.g. "22.99"
    val currentPrice: String, // e.g. "12.99"
    val bagType: String, // e.g. "Small Bag" (green text)
    val category: String, // e.g. "Snacks and Drinks" (black text)
    val restaurantName: String, // Merchant name for logo placeholder
    val restaurantLogoUrl: String? = null,
    val pickupTime: String, // e.g. "Pick up from 17:00 to 23:00"
    val rating: Float = 0f,
    val distance: String, // e.g. "1.2 km"
    val itemsLeft: Int = 0 // e.g. 12 for "12 left" badge
)

