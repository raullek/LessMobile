package az.less.mobile.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Offers screen response from API
 * GET /api/v1/home/mobile
 */
@Serializable
data class OffersScreenDto(
    val categories: List<CategoryDto> = emptyList(),
    val specialCategories: List<SpecialCategoryDto> = emptyList(),
    val specialSegments: List<SpecialSegmentDto> = emptyList(),
    val homepageButtons: List<HomepageButtonDto> = emptyList()
)

@Serializable
data class CategoryDto(
    val id: String,
    val type: String? = null,
    val title: String? = null,
    val titles: String? = null,
    val imageUrl: String? = null,
    val filters: List<CategoryFilterDto> = emptyList(),
    val searchUrl: String? = null
)

@Serializable
data class CategoryFilterDto(
    val searchFilterId: String,
    val values: List<String> = emptyList()
)

@Serializable
data class SpecialCategoryDto(
    val id: String,
    val type: String? = null,
    val title: String? = null,
    val titles: String? = null,
    val description: String? = null,
    val descriptions: String? = null,
    val imageUrl: String? = null,
    val filters: List<CategoryFilterDto> = emptyList(),
    val searchUrl: String? = null
)

@Serializable
data class SpecialSegmentDto(
    val id: String,
    val title: String? = null,
    val titles: String? = null,
    val order: Int = 0,
    val filters: List<CategoryFilterDto> = emptyList(),
    val searchUrl: String? = null,
    val boxes: List<OfferDto> = emptyList()
)

@Serializable
data class HomepageButtonDto(
    val id: String,
    val type: String? = null,
    val title: String? = null,
    val titles: String? = null,
    val searchUrl: String? = null
)

@Serializable
data class OfferDto(
    val id: String,
    val title: String? = null,
    val description: String? = null,
    val defaultBoxTitle: String? = null,
    val defaultBoxDescription: String? = null,
    val imageUrl: String? = null,
    val imageBgColor: String? = null,
    val quantity: Int = 0,
    val originalPrice: Double = 0.0,
    val currentPrice: Double = 0.0,
    val bagType: String? = null,
    val category: String? = null,
    val pickupTime: String? = null,
    @SerialName("venue")
    val venue: VenueDto? = null
)

@Serializable
data class VenueDto(
    val id: String,
    val name: String? = null,
    val logoUrl: String? = null,
    val location: LocationDto? = null,
    val rating: Double = 0.0,
    val distanceText: String? = null,
    val distanceKm: Double? = null
)

@Serializable
data class LocationDto(
    val type: String? = null,
    val coordinates: List<Double> = emptyList()
)
