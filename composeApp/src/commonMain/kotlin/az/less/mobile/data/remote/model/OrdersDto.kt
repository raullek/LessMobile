package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

/**
 * Orders response from API
 * GET /api/v1/client/orders?type=active|previous&page=&limit=
 */
@Serializable
data class OrdersDataDto(
    val data: List<OrderItemDto>,
    val pagination: OrdersPaginationDto? = null
)

@Serializable
data class OrderItemDto(
    val id: String,
    val orderNumber: String? = null,
    val status: String? = null,
    val reserveNumber: String? = null,
    val quantity: Int = 1,
    val price: Double = 0.0,
    val pricePerPiece: Double = 0.0,
    val serviceFee: Double = 0.0,
    val subtotal: Double = 0.0,
    val currency: String? = null,
    val pickupTimeStart: String? = null,
    val pickupTimeEnd: String? = null,
    val pickupTimeFormatted: String? = null,
    val dateFormatted: String? = null,
    val completedAt: String? = null,
    val createdAt: String? = null,
    val venue: OrderVenueDto? = null,
    val item: OrderItemSnapshotDto? = null
)

@Serializable
data class OrderVenueDto(
    val id: String? = null,
    val name: String? = null,
    val logo: String? = null,
    val address: String? = null
)

@Serializable
data class OrderItemSnapshotDto(
    val name: String? = null,
    val image: String? = null
)

@Serializable
data class OrdersPaginationDto(
    val page: Int,
    val limit: Int,
    val total: Int,
    val totalPages: Int,
    val hasNext: Boolean = false,
    val hasPrev: Boolean = false
)
