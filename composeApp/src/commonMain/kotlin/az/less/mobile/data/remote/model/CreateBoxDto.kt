package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

/**
 * Request body for POST /api/v1/boxes
 */
@Serializable
data class CreateBoxRequest(
    val description: String,
    val boxType: String,
    val categoryId: String,
    val tagIds: List<String> = emptyList(),
    val pickupRange: String,
    val originalPrice: Double,
    val discountedPrice: Double,
    val quantity: Int
)

/**
 * Response data from POST /api/v1/boxes
 * Extracted from ApiResponse<CreateBoxResponseDto>.data
 */
@Serializable
data class CreateBoxResponseDto(
    val success: Boolean? = null,
    val message: String? = null,
    val warning: String? = null,
    val box: BoxResponseDto? = null
)

@Serializable
data class BoxResponseDto(
    val id: String,
    val title: String? = null,
    val description: String? = null,
    val defaultBoxTitle: String? = null,
    val defaultBoxDescription: String? = null,
    val originalPrice: Double,
    val discountedPrice: Double,
    val quantity: Int,
    val availableItems: Int? = null,
    val image: String? = null,
    val defaultBoxImage: String? = null,
    val boxType: String? = null,
    val categoryId: String? = null,
    val tagIds: List<String> = emptyList(),
    val pickupRange: String? = null,
    val pickupTimeStart: String? = null,
    val pickupTimeEnd: String? = null,
    val createdAt: String? = null
)
