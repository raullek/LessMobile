package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

/**
 * Venues response from API
 * GET /api/v1/venues?page=&limit=
 */
@Serializable
data class VenuesDataDto(
    val data: List<AdminVenueDto>,
    val pagination: VenuesPaginationDto? = null
)

@Serializable
data class AdminVenueDto(
    val id: String,
    val name: String,
    val businessName: String? = null,
    val businessAddress: String? = null,
    val location: VenueLocationDto? = null,
    val businessDescription: String? = null,
    val businessLogo: String? = null,
    val coverImage: String? = null,
    val lotImage: String? = null,
    val defaultBoxTitle: String? = null,
    val defaultBoxDescription: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val createdBy: String? = null,
    val adminUserIds: List<String> = emptyList(),
    val status: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val boxCounts: VenueBoxCountsDto? = null
)

@Serializable
data class VenueBoxCountsDto(
    val available: Int = 0,
    val sold_out: Int = 0,
    val expired: Int = 0,
    val deleted: Int = 0,
    val total: Int = 0
)

@Serializable
data class VenuesPaginationDto(
    val page: Int,
    val limit: Int,
    val total: Int,
    val totalPages: Int,
    val hasNext: Boolean = false,
    val hasPrev: Boolean = false
)