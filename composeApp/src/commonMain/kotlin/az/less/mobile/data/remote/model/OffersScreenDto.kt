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
    val type: String,
    val title: String,
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
    val type: String,
    val title: String,
    val titles: String? = null,
    val description: String,
    val descriptions: String? = null,
    val imageUrl: String? = null,
    val filters: List<CategoryFilterDto> = emptyList(),
    val searchUrl: String? = null
)

@Serializable
data class SpecialSegmentDto(
    val id: String,
    val title: String,
    val titles: String? = null,
    val order: Int = 0,
    val filters: List<CategoryFilterDto> = emptyList(),
    val searchUrl: String? = null,
    val boxes: List<OfferDto> = emptyList()
)

@Serializable
data class HomepageButtonDto(
    val id: String,
    val type: String,
    val title: String,
    val titles: String? = null,
    val searchUrl: String? = null
)

@Serializable
data class OfferDto(
    val id: String,
    val title: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val imageBgColor: String? = null,
    val quantity: Int,
    val originalPrice: Double,
    val currentPrice: Double,
    val bagType: String? = null,
    val category: String,
    val pickupTime: String,
    @SerialName("venue")
    val venue: VenueDto
)

@Serializable
data class VenueDto(
    val id: String,
    val name: String,
    val logoUrl: String? = null,
    val location: LocationDto,
    val rating: Double,
    val distanceText: String? = null,
    val distanceKm: Double? = null
)

@Serializable
data class LocationDto(
    val type: String,
    val coordinates: List<Double>
)
