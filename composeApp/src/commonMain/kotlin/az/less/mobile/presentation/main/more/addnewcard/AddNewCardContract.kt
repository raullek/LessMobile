package az.less.mobile.presentation.main.more.paymentmethods.addnewcard

data class AddNewCardState(
    val isLoading: Boolean = false,
    val cardNumber: String = "",
    val expirationDate: String = "",
    val cvv: String = "",
    val cardNumberError: String? = null,
    val expirationDateError: String? = null,
    val cvvError: String? = null
)

interface AddNewCardSideEffect {
    data object NavigateBack : AddNewCardSideEffect
    data object NavigateToSuccess : AddNewCardSideEffect
    data class ShowError(val message: String) : AddNewCardSideEffect
}

sealed interface AddNewCardIntent {
    data object OnBackClicked : AddNewCardIntent
    data class OnCardNumberChanged(val value: String) : AddNewCardIntent
    data class OnExpirationDateChanged(val value: String) : AddNewCardIntent
    data class OnCvvChanged(val value: String) : AddNewCardIntent
    data object OnAddCardClicked : AddNewCardIntent
}

