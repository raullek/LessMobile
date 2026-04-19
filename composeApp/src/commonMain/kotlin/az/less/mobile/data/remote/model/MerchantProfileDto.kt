package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

/**
 * Merchant profile response from API
 * GET /api/v1/venues/{id}
 */
@Serializable
data class MerchantProfileDto(
    val id: String? = null,
    val name: String? = null,
    val businessName: String? = null,
    val businessAddress: String? = null,
    val businessDescription: String? = null,
    val businessLogo: String? = null,
    val coverImage: String? = null,
    val lotImage: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val location: LocationDto? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val distanceKm: Double? = null,
    val rating: VenueRatingDto? = null,
    val reviews: List<VenueReviewItemDto> = emptyList(),
    val offers: List<VenueOfferDto> = emptyList(),
    val businessHours: BusinessHoursDto? = null,
    val status: String? = null,
    val isFavorite: Boolean = false,
    val favoriteId: String? = null
)

@Serializable
data class VenueRatingDto(
    val average: Double = 0.0,
    val averageDisplay: String? = null,
    val total: Int = 0,
    val distribution: RatingDistributionDto? = null
)

@Serializable
data class RatingDistributionDto(
    val withPercentages: Map<String, RatingPercentageDto>? = null
)

@Serializable
data class RatingPercentageDto(
    val count: Int = 0,
    val percentage: Double = 0.0
)

@Serializable
data class VenueReviewItemDto(
    val id: String? = null,
    val rating: Int? = null,
    val comment: String? = null,
    val createdAt: String? = null,
    val client: VenueReviewClientDto? = null,
    val reply: VenueReviewReplyDto? = null
)

@Serializable
data class VenueReviewClientDto(
    val id: String? = null,
    val name: String? = null,
    val avatar: String? = null
)

@Serializable
data class VenueReviewReplyDto(
    val text: String? = null,
    val repliedAt: String? = null,
    val venueLogo: String? = null
)

@Serializable
data class BusinessHoursDto(
    val monday: DayHoursDto? = null,
    val tuesday: DayHoursDto? = null,
    val wednesday: DayHoursDto? = null,
    val thursday: DayHoursDto? = null,
    val friday: DayHoursDto? = null,
    val saturday: DayHoursDto? = null,
    val sunday: DayHoursDto? = null
)

@Serializable
data class DayHoursDto(
    val open: String? = null,
    val close: String? = null
)

@Serializable
data class VenueOfferDto(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val images: List<String> = emptyList(),
    val originalPrice: Double = 0.0,
    val discountedPrice: Double = 0.0,
    val availableItems: Int = 0,
    val pickupTimeStart: String? = null,
    val pickupTimeEnd: String? = null,
    val category: BoxDetailCategoryDto? = null
)
