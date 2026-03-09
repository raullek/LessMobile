package az.less.mobile.presentation.client.reserve.models

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
