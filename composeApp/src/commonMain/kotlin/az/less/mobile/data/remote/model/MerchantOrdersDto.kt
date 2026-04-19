package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

/**
 * Bought boxes response from API
 * GET /api/v1/orders/merchant-orders/bought-boxes
 */
@Serializable
data class BoughtBoxesResponse(
    val title: String? = null,
    val data: List<BoughtBoxDto> = emptyList(),
    val total: Int? = null
)

/**
 * Created boxes response from API
 * GET /api/v1/orders/merchant-orders/created-boxes
 */
@Serializable
data class CreatedBoxesResponse(
    val title: String? = null,
    val data: List<CreatedBoxDto> = emptyList(),
    val total: Int? = null
)

@Serializable
data class BoughtBoxDto(
    val id: String,
    val orderId: String? = null,
    val reserveNumber: String? = null,
    val box: BoughtBoxInfoDto? = null,
    val client: BoxClientDto? = null,
    val quantity: Int? = null,
    val subtotal: Double? = null,
    val status: String? = null,
    val pickupTimeStart: String? = null,
    val pickupTimeEnd: String? = null,
    val pickupTimeFormatted: String? = null,
    val createdAt: String? = null,
    val canDeliver: Boolean? = null
)

@Serializable
data class BoughtBoxInfoDto(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val defaultBoxTitle: String? = null,
    val defaultBoxDescription: String? = null,
    val image: String? = null,
    val defaultBoxImage: String? = null,
    val originalPrice: Double? = null,
    val discountedPrice: Double? = null,
    val boxType: String? = null,
    val category: String? = null
)

@Serializable
data class BoxClientDto(
    val id: String? = null,
    val name: String? = null,
    val avatar: String? = null
)

@Serializable
data class CreatedBoxDto(
    val id: String,
    val title: String? = null,
    val description: String? = null,
    val defaultBoxTitle: String? = null,
    val defaultBoxDescription: String? = null,
    val image: String? = null,
    val defaultBoxImage: String? = null,
    val originalPrice: Double? = null,
    val discountedPrice: Double? = null,
    val quantity: Int? = null,
    val soldCount: Int? = null,
    val availableItems: Int? = null,
    val boxType: String? = null,
    val category: String? = null,
    val tags: List<String> = emptyList(),
    val pickupTimeStart: String? = null,
    val pickupTimeEnd: String? = null,
    val pickupTimeFormatted: String? = null,
    val status: String? = null,
    val isActive: Boolean? = null,
    val createdAt: String? = null,
    val canCancel: Boolean? = null,
    val timeRemaining: String? = null,
    val timeRemainingSeconds: Int? = null,
    val cancellationExpired: Boolean? = null,
    val closeTimeFormatted: String? = null,
    val cancellationMessage: String? = null,
    val cancelButtonText: String? = null
)
