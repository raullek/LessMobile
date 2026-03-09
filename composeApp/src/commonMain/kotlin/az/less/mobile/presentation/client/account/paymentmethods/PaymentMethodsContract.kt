package az.less.mobile.presentation.client.account.paymentmethods

data class PaymentMethod(
    val id: String,
    val type: PaymentMethodType,
    val lastFourDigits: String? = null,
    val isSelected: Boolean = false
)

enum class PaymentMethodType {
    MASTERCARD,
    VISA,
    ADD_NEW_CARD
}

data class PaymentMethodsState(
    val isLoading: Boolean = false,
    val isRegisterCardLoading: Boolean = false,
    val creditDebitCards: List<PaymentMethod> = emptyList(),
    val cardToDelete: PaymentMethod? = null
)

interface PaymentMethodsSideEffect {
    data object NavigateBack : PaymentMethodsSideEffect
    data class OpenAddCardWebView(val url: String) : PaymentMethodsSideEffect
    data class ShowError(val message: String) : PaymentMethodsSideEffect
}

sealed interface PaymentMethodsIntent {
    data object OnBackClicked : PaymentMethodsIntent
    data class OnCardSelected(val cardId: String) : PaymentMethodsIntent
    data class OnDeleteCardClicked(val cardId: String) : PaymentMethodsIntent
    data object OnConfirmDelete : PaymentMethodsIntent
    data object OnCancelDelete : PaymentMethodsIntent
    data object OnAddNewCardClicked : PaymentMethodsIntent
    data object LoadCards : PaymentMethodsIntent
}
