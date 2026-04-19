package az.less.mobile.domain.model

data class PaymentMethod(
    val id: String,
    val brand: CardBrand,
    val lastFourDigits: String,
    val displayName: String,
    val isDefault: Boolean,
    val message: String? = null
)

enum class CardBrand {
    VISA,
    MASTERCARD,
    UNKNOWN
}
