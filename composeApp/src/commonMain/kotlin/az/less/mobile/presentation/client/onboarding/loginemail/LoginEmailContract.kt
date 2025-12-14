package az.less.mobile.presentation.client.onboarding.loginemail

data class LoginEmailState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false
)

sealed interface LoginEmailIntent {
    data object OnBackClicked : LoginEmailIntent
    data class OnEmailChanged(val email: String) : LoginEmailIntent
    data object OnNextClicked : LoginEmailIntent
}

sealed interface LoginEmailSideEffect {
    data object NavigateBack : LoginEmailSideEffect
    data object NavigateNext : LoginEmailSideEffect
    data class ShowError(val message: String) : LoginEmailSideEffect
}

