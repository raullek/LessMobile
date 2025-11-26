package az.less.mobile.presentation.main.explore.models

data class ExploreVenueItem(
    val id: String,
    val title: String,
    val imageUrl: String? = null,
    val merchantIconUrl: String? = null,
    val price: String, // e.g. "12.99"
    val pickupTime: String, // e.g. "Pick up from 17:00 to 23:00"
    val rating: Float = 0f,
    val reviewCount: String = "", // e.g. "28+"
    val distance: String // e.g. "1.2 km away"
)



