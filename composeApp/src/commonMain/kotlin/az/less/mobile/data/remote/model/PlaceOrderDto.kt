package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaceOrderRequest(
    val boxId: String,
    val quantity: Int,
    val paymentMethodId: String,
    val notes: String? = null,
    val userVoucherId: String? = null
)

@Serializable
data class PlaceOrderData(
    val order: PlaceOrderInfo? = null
)

@Serializable
data class PlaceOrderInfo(
    val id: String = "",
    val reserveNumber: String? = null,
    val status: String? = null,
    val subtotal: Double = 0.0,
    val createdAt: String? = null
)
