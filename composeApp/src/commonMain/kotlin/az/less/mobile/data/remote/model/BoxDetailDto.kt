package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

/**
 * Box detail response from API
 * GET /api/v1/boxes/{id}
 */
@Serializable
data class BoxDetailDto(
    val id: String,
    val title: String? = null,
    val description: String? = null,
    val defaultBoxTitle: String? = null,
    val defaultBoxDescription: String? = null,
    val originalPrice: Double = 0.0,
    val discountedPrice: Double = 0.0,
    val quantity: Int = 0,
    val soldCount: Int = 0,
    val status: String? = null,
    val boxType: String? = null,
    val categoryId: String? = null,
    val category: BoxDetailCategoryDto? = null,
    val pickupTimeStart: String? = null,
    val pickupTimeEnd: String? = null,
    val pickupTimeFormatted: String? = null,
    val images: List<String> = emptyList(),
    val dietaryInfo: List<String> = emptyList(),
    val tagIds: List<String> = emptyList(),
    val tags: List<BoxTagDto> = emptyList(),
    val isOpenNow: Boolean = false,
    val venue: BoxVenueDto? = null,
    val location: LocationDto? = null,
    val address: String? = null,
    val collectionNotes: String? = null
)

@Serializable
data class BoxDetailCategoryDto(
    val id: String = "",
    val value: String = "",
    val title: String = "",
    val imageUrl: String? = null,
    val sortOrder: Int = 0
)

@Serializable
data class BoxVenueDto(
    val id: String,
    val name: String? = null,
    val businessName: String? = null,
    val businessLogo: String? = null,
    val businessAddress: String? = null,
    val phone: String? = null
)

@Serializable
data class BoxTagDto(
    val id: String? = null,
    val value: String? = null,
    val title: String? = null,
    val imageUrl: String? = null
)
