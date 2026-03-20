package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

/**
 * Merchant orders response from API
 * GET /api/v1/orders/merchant-orders?venueId=...
 */
@Serializable
data class MerchantOrdersData(
    val boughtBoxes: BoughtBoxesSection,
    val createdBoxes: CreatedBoxesSection
)

@Serializable
data class BoughtBoxesSection(
    val title: String,
    val data: List<BoughtBoxDto> = emptyList(),
    val total: Int = 0
)

@Serializable
data class CreatedBoxesSection(
    val title: String,
    val data: List<CreatedBoxDto> = emptyList(),
    val total: Int = 0
)

@Serializable
data class BoughtBoxDto(
    val id: String,
    val orderId: String,
    val reserveNumber: String,
    val box: BoughtBoxInfoDto,
    val client: BoxClientDto,
    val quantity: Int = 1,
    val subtotal: Double = 0.0,
    val status: String,
    val pickupTimeStart: String? = null,
    val pickupTimeEnd: String? = null,
    val pickupTimeFormatted: String? = null,
    val createdAt: String? = null,
    val canDeliver: Boolean = false
)

@Serializable
data class BoughtBoxInfoDto(
    val id: String,
    val title: String,
    val images: List<String> = emptyList(),
    val originalPrice: Double = 0.0,
    val discountedPrice: Double = 0.0,
    val boxType: String? = null,
    val categoryId: String? = null
)

@Serializable
data class BoxClientDto(
    val id: String,
    val name: String,
    val avatar: String? = null
)

@Serializable
data class CreatedBoxDto(
    val id: String,
    val title: String,
    val description: String? = null,
    val images: List<String> = emptyList(),
    val originalPrice: Double = 0.0,
    val discountedPrice: Double = 0.0,
    val quantity: Int = 0,
    val soldCount: Int = 0,
    val availableItems: Int = 0,
    val boxType: String? = null,
    val categoryId: String? = null,
    val tagIds: List<String> = emptyList(),
    val pickupTimeStart: String? = null,
    val pickupTimeEnd: String? = null,
    val pickupTimeFormatted: String? = null,
    val status: String? = null,
    val isActive: Boolean = true,
    val createdAt: String? = null,
    val canCancel: Boolean = false,
    val timeRemaining: String? = null,
    val timeRemainingSeconds: Int = 0,
    val cancellationExpired: Boolean = false,
    val closeTimeFormatted: String? = null,
    val cancellationMessage: String? = null,
    val cancelButtonText: String? = null
)
