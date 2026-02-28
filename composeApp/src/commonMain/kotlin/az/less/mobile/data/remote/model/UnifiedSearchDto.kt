package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UnifiedSearchDto(
    val venues: List<SearchVenueDto> = emptyList(),
    val boxes: List<SearchBoxDto> = emptyList(),
    val total: Int = 0
)

@Serializable
data class SearchVenueDto(
    val id: String,
    val name: String,
    val logo: String? = null,
    val address: String? = null,
    val description: String? = null,
    val location: LocationDto,
    val distanceKm: Double? = null,
    val rating: Double = 0.0,
    val totalReviews: Int = 0,
    val activeBoxes: Int = 0,
    val itemsOnSale: Int = 0,
    val hasActiveOffers: Boolean = false,
    val badge: VenueBadgeDto? = null,
    val ratingAndDistance: String? = null
)

@Serializable
data class SearchBoxDto(
    val id: String,
    val title: String,
    val description: String? = null,
    val images: List<String> = emptyList(),
    val originalPrice: Double = 0.0,
    val discountedPrice: Double = 0.0,
    val availableItems: Int = 0,
    val venue: SearchBoxVenueDto
)

@Serializable
data class SearchBoxVenueDto(
    val id: String,
    val name: String,
    val logo: String? = null,
    val address: String? = null,
    val location: LocationDto,
    val distanceKm: Double? = null
)
