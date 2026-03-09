package az.less.mobile.presentation.client.account.paymentmethods.addcard

data class AddCardWebViewState(
    val isVerifying: Boolean = false,
    val showSuccessToast: Boolean = false,
    val showErrorToast: Boolean = false
)

sealed interface AddCardWebViewSideEffect {
    data object CardAddedSuccessfully : AddCardWebViewSideEffect
    data object CardAdditionFailed : AddCardWebViewSideEffect
    data class ShowError(val message: String) : AddCardWebViewSideEffect
}

sealed interface AddCardWebViewIntent {
    data class OnCallbackUrlDetected(val callbackUrl: String) : AddCardWebViewIntent
    data class OnErrorCallbackDetected(val callbackUrl: String) : AddCardWebViewIntent
    data object OnBackClicked : AddCardWebViewIntent
}
