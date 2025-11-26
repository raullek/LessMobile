package az.less.mobile.presentation.reserve.models

/**
 * Payment card model
 */
data class PaymentCard(
    val id: String,
    val type: CardType,
    val lastFourDigits: String,
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

