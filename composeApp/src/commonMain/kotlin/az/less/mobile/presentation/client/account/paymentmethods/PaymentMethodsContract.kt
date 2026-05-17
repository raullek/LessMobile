package az.less.mobile.presentation.client.account.paymentmethods

import az.less.designsystem.components.ToastType
import az.less.mobile.domain.model.CardBrand

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
    val isDeleting: Boolean = false,
    val creditDebitCards: List<PaymentMethod> = emptyList(),
    val cardToDelete: PaymentMethod? = null
)

sealed interface PaymentMethodsSideEffect {
    data object NavigateBack : PaymentMethodsSideEffect
    data class OpenAddCardWebView(val url: String) : PaymentMethodsSideEffect
    data class ShowToast(val message: String, val type: ToastType) : PaymentMethodsSideEffect
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

fun CardBrand.toPaymentMethodType() = when (this) {
    CardBrand.VISA -> PaymentMethodType.VISA
    CardBrand.MASTERCARD -> PaymentMethodType.MASTERCARD
    CardBrand.UNKNOWN -> PaymentMethodType.VISA
}

fun az.less.mobile.domain.model.PaymentMethod.toPresentation() = PaymentMethod(
    id = id,
    type = brand.toPaymentMethodType(),
    lastFourDigits = lastFourDigits,
    isSelected = isDefault
)
