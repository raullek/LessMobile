package az.less.mobile.presentation.client.onboarding.otp

private const val OTP_LENGTH = 6

data class LoginCodeState(
    val email: String = "",
    val code: String = "",
    val isLoading: Boolean = false,
    val codeError: String? = null
) {
    val isCodeValid: Boolean
        get() = code.length == OTP_LENGTH
}

sealed interface LoginCodeIntent {
    data object OnBackClicked : LoginCodeIntent
    data class OnCodeChanged(val code: String) : LoginCodeIntent
    data object OnVerifyClicked : LoginCodeIntent
    data object OnResendCodeClicked : LoginCodeIntent
}

sealed interface LoginCodeSideEffect {
    data object NavigateBack : LoginCodeSideEffect
    data object NavigateToClient : LoginCodeSideEffect
    data object NavigateToMerchant : LoginCodeSideEffect
    data class ShowSuccess(val message: String) : LoginCodeSideEffect
    data class ShowError(val message: String) : LoginCodeSideEffect
}
