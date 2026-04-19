package az.less.mobile.presentation.client.reserve.models

import az.less.mobile.domain.model.CardBrand
import az.less.mobile.domain.model.PaymentMethod

/**
 * Payment card model
 */
data class PaymentCard(
    val id: String,
    val type: CardType,
    val lastFourDigits: String,
    val brand: String = "",
    val displayName: String = "",
    val isSelected: Boolean = false
)

/**
 * Card types
 */
enum class CardType {
    MASTERCARD,
    VISA,
    ADD_NEW
}

fun CardBrand.toCardType() = when (this) {
    CardBrand.VISA -> CardType.VISA
    CardBrand.MASTERCARD -> CardType.MASTERCARD
    CardBrand.UNKNOWN -> CardType.VISA
}

fun PaymentMethod.toPaymentCard(isSelected: Boolean = isDefault) = PaymentCard(
    id = id,
    type = brand.toCardType(),
    lastFourDigits = lastFourDigits,
    brand = brand.name.lowercase(),
    displayName = displayName,
    isSelected = isSelected
)
