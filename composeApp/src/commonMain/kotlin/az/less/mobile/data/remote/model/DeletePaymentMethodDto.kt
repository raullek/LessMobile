package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class DeletePaymentMethodDto(
    val message: String? = null,
    val deletedCard: String? = null,
    val newDefaultSet: Boolean = false
)
