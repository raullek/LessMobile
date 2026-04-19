package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

/**
 * Response from POST /api/v1/orders/{id}/deliver
 */
@Serializable
data class OrderDeliveryResponse(
    val message: String? = null,
    val order: DeliveredOrderDto? = null
)

@Serializable
data class DeliveredOrderDto(
    val id: String? = null,
    val reserveNumber: String? = null,
    val quantity: Int? = null,
    val totalPrice: Double? = null,
    val discountAmount: Double? = null,
    val status: String? = null,
    val completedAt: String? = null,
    val client: BoxClientDto? = null,
    val box: DeliveredBoxDto? = null,
    val voucher: DeliveredVoucherDto? = null
)

@Serializable
data class DeliveredBoxDto(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val defaultBoxTitle: String? = null,
    val defaultBoxDescription: String? = null
)

@Serializable
data class DeliveredVoucherDto(
    val userVoucherId: String? = null,
    val code: String? = null,
    val type: String? = null,
    val value: Double? = null,
    val currency: String? = null,
    val discountAmount: Double? = null
)
