package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

/**
 * Favorites response from API
 * GET /api/v1/favorites?latitude=&longitude=&page=&limit=
 */
@Serializable
data class FavoritesDataDto(
    val data: List<FavoriteItemDto>,
    val pagination: FavoritesPaginationDto? = null,
    val isEmpty: Boolean = false,
    val message: String? = null,
    val description: String? = null
)

@Serializable
data class FavoriteItemDto(
    val id: String,
    val type: String,
    val itemId: String,
    val itemSnapshot: ItemSnapshotDto? = null,
    val venue: FavoriteVenueDto? = null,
    val createdAt: String? = null
)

@Serializable
data class ItemSnapshotDto(
    val name: String? = null,
    val businessName: String? = null,
    val logo: String? = null,
    val address: String? = null
)

@Serializable
data class FavoriteVenueDto(
    val id: String,
    val name: String,
    val logo: String? = null,
    val address: String? = null,
    val description: String? = null,
    val location: VenueLocationDto? = null,
    val rating: Double = 0.0,
    val distanceKm: Double? = null,
    val ratingAndDistance: String? = null,
    val activeBoxes: Int = 0,
    val availableBoxes: Int = 0,
    val itemsOnSale: Int = 0,
    val badge: VenueBadgeDto? = null
)

@Serializable
data class VenueLocationDto(
    val type: String? = null,
    val coordinates: List<Double> = emptyList()
)

@Serializable
data class VenueBadgeDto(
    val text: String? = null,
    val type: String? = null,
    val color: String? = null
)

@Serializable
data class FavoritesPaginationDto(
    val page: Int,
    val limit: Int,
    val total: Int,
    val totalPages: Int,
    val hasNext: Boolean = false,
    val hasPrev: Boolean = false
)
