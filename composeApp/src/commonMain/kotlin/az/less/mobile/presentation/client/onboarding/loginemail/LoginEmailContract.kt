package az.less.mobile.presentation.client.onboarding.loginemail

private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

data class LoginEmailState(
    val email: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null
) {
    val isEmailValid: Boolean
        get() = email.isNotEmpty() && EMAIL_REGEX.matches(email)
}

sealed interface LoginEmailIntent {
    data object OnBackClicked : LoginEmailIntent
    data class OnEmailChanged(val email: String) : LoginEmailIntent
    data object OnNextClicked : LoginEmailIntent
}

sealed interface LoginEmailSideEffect {
    data object NavigateBack : LoginEmailSideEffect
    data object NavigateNext : LoginEmailSideEffect
}
