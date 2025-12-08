package az.less.mobile.presentation.onboarding.otp

data class LoginCodeState(
    val code: String = "",
    val codeError: String? = null,
    val isLoading: Boolean = false,
    val email: String = "" // Email from previous screen
)

sealed interface LoginCodeIntent {
    data object OnBackClicked : LoginCodeIntent
    data class OnCodeChanged(val code: String) : LoginCodeIntent
    data object OnVerifyClicked : LoginCodeIntent
    data object OnResendCodeClicked : LoginCodeIntent
}

sealed interface LoginCodeSideEffect {
    data object NavigateBack : LoginCodeSideEffect
    data object NavigateNext : LoginCodeSideEffect
    data class ShowError(val message: String) : LoginCodeSideEffect
    data class ShowSuccess(val message: String) : LoginCodeSideEffect
}

