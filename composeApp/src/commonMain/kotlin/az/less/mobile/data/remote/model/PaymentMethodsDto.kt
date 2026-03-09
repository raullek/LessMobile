package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethodsDto(
    val cards: List<PaymentCardDto> = emptyList(),
    val otherMethods: List<OtherPaymentMethodDto> = emptyList()
)

@Serializable
data class PaymentCardDto(
    val id: String,
    val type: String? = null,
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

@Serializable
data class OtherPaymentMethodDto(
    val id: String,
    val type: String,
    val name: String? = null,
    val logo: String? = null,
    val enabled: Boolean = false
)
