package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

/**
 * Response DTO when searching with type=venues
 * GET /api/v1/search/by-filters?type=venues
 */
@Serializable
data class SearchByFilterVenuesDto(
    val venues: List<SearchVenueDto> = emptyList(),
    val total: Int = 0,
    val pagination: SearchPaginationDto? = null
)

/**
 * Response DTO when searching with type=boxes
 * GET /api/v1/search/by-filters?type=boxes
 *
 * Uses [FilterBoxDto] which does NOT require the venue field,
 * since venue data is only included when returnVenueForBox=true.
 */
@Serializable
data class SearchByFilterBoxesDto(
    val boxes: List<FilterBoxDto> = emptyList(),
    val total: Int = 0,
    val pagination: SearchPaginationDto? = null
)

/**
 * Box DTO returned by /search/by-filters.
 * The venue field is optional — only present when returnVenueForBox=true.
 */
@Serializable
data class FilterBoxDto(
    val id: String,
    val title: String,
    val description: String? = null,
    val images: List<String> = emptyList(),
    val imageUrl: String? = null,
    val imageBgColor: String? = null,
    val originalPrice: Double = 0.0,
    val discountedPrice: Double = 0.0,
    val discountPercentage: Double = 0.0,
    val currentPrice: Double? = null,
    val quantity: Int = 0,
    val soldCount: Int = 0,
    val availableItems: Int = 0,
    val bagType: String? = null,
    val boxType: String? = null,
    val categoryId: String? = null,
    val category: FilterBoxCategoryDto? = null,
    val tagIds: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val isOpenNow: Boolean = false,
    val pickupTime: String? = null,
    val pickupTimeStart: String? = null,
    val pickupTimeEnd: String? = null,
    val pickupTimeFormatted: String? = null,
    val venue: SearchBoxVenueDto? = null
)

@Serializable
data class FilterBoxCategoryDto(
    val id: String = "",
    val value: String = "",
    val title: String = "",
    val imageUrl: String? = null,
    val sortOrder: Int = 0
)

@Serializable
data class SearchPaginationDto(
    val page: Int = 1,
    val limit: Int = 20,
    val venuesTotal: Int = 0,
    val boxesTotal: Int = 0,
    val total: Int = 0
)

/**
 * Filter parameter sent as JSON in the "filters" query param.
 * Example: [{"searchFilterId":"674a...","values":["today"]}]
 */
@Serializable
data class SearchFilterParam(
    val searchFilterId: String,
    val values: List<String>
)

/**
 * Response DTO for GET /api/v1/search/venues
 * The API wraps the venues list inside a nested "data" object.
 */
@Serializable
data class SearchVenuesResponseDto(
    val data: List<SearchVenueDto> = emptyList()
)
