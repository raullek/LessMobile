package az.less.mobile.presentation.main.more.paymentmethods

data class PaymentMethod(
    val id: String,
    val type: PaymentMethodType,
    val lastFourDigits: String? = null,
    val isSelected: Boolean = false
)

enum class PaymentMethodType {
    MASTERCARD,
    VISA,
    APPLE_PAY,
    GOOGLE_PAY,
    ADD_NEW_CARD
}

data class PaymentMethodsState(
    val isLoading: Boolean = false,
    val creditDebitCards: List<PaymentMethod> = listOf(
        PaymentMethod(
            id = "1",
            type = PaymentMethodType.MASTERCARD,
            lastFourDigits = "2412",
            isSelected = true
        ),
        PaymentMethod(
            id = "2",
            type = PaymentMethodType.VISA,
            lastFourDigits = "3440",
            isSelected = false
        )
    ),
    val otherMethods: List<PaymentMethod> = listOf(
        PaymentMethod(
            id = "3",
            type = PaymentMethodType.APPLE_PAY,
            isSelected = false
        ),
        PaymentMethod(
            id = "4",
            type = PaymentMethodType.GOOGLE_PAY,
            isSelected = false
        )
    ),
    val cardToDelete: PaymentMethod? = null
)

interface PaymentMethodsSideEffect {
    data object NavigateBack : PaymentMethodsSideEffect
    data object NavigateToAddCard : PaymentMethodsSideEffect
    data object NavigateToApplePay : PaymentMethodsSideEffect
    data object NavigateToGooglePay : PaymentMethodsSideEffect
    data class ShowError(val message: String) : PaymentMethodsSideEffect
}

sealed interface PaymentMethodsIntent {
    data object OnBackClicked : PaymentMethodsIntent
    data class OnCardSelected(val cardId: String) : PaymentMethodsIntent
    data class OnDeleteCardClicked(val cardId: String) : PaymentMethodsIntent
    data object OnConfirmDelete : PaymentMethodsIntent
    data object OnCancelDelete : PaymentMethodsIntent
    data object OnAddNewCardClicked : PaymentMethodsIntent
    data object OnApplePayClicked : PaymentMethodsIntent
    data object OnGooglePayClicked : PaymentMethodsIntent
}



