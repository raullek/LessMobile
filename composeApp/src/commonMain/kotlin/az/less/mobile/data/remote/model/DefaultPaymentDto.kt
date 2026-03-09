package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class DefaultPaymentDto(
    val id: String,
    val type: String,
    val last4: String? = null,
    val cardMask: String? = null,
    val brand: String? = null,
    val expiryMonth: Int? = null,
    val expiryYear: Int? = null,
    val isDefault: Boolean = false,
    val isVerified: Boolean = false,
    val cardholderName: String? = null,
    val displayName: String? = null,
    val status: String? = null,
    val expiryDate: String? = null
)
