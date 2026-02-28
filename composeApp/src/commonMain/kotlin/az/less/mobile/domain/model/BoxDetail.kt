package az.less.mobile.domain.model

/**
 * Domain model for box detail
 */
data class BoxDetail(
    val id: String,
    val title: String,
    val description: String,
    val originalPrice: Double,
    val discountedPrice: Double,
    val availableQuantity: Int,
    val status: String,
    val boxType: String,
    val category: String,
    val pickupTimeFormatted: String,
    val images: List<String>,
    val dietaryInfo: List<String>,
    val tags: List<String>,
    val venue: BoxVenue,
    val address: String,
    val collectionNotes: String
)

data class BoxVenue(
    val id: String,
    val name: String,
    val businessName: String,
    val businessLogo: String?,
    val businessAddress: String,
    val phone: String
)
