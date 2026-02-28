package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

/**
 * Box detail response from API
 * GET /api/v1/boxes/{id}
 */
@Serializable
data class BoxDetailDto(
    val id: String,
    val title: String,
    val description: String? = null,
    val originalPrice: Double,
    val discountedPrice: Double,
    val quantity: Int,
    val soldCount: Int = 0,
    val status: String? = null,
    val boxType: String? = null,
    val category: String? = null,
    val pickupTimeStart: String? = null,
    val pickupTimeEnd: String? = null,
    val images: List<String> = emptyList(),
    val dietaryInfo: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val venue: BoxVenueDto,
    val location: LocationDto? = null,
    val address: String? = null,
    val collectionNotes: String? = null
)

@Serializable
data class BoxVenueDto(
    val id: String,
    val name: String,
    val businessName: String? = null,
    val businessLogo: String? = null,
    val businessAddress: String? = null,
    val phone: String? = null
)
