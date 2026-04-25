package az.less.mobile.presentation.client.onboarding.loginpassword

private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

data class LoginPasswordState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
) {
    val isEmailValid: Boolean
        get() = email.isNotEmpty() && EMAIL_REGEX.matches(email)

    val isFormValid: Boolean
        get() = isEmailValid && password.isNotEmpty()
}

sealed interface LoginPasswordIntent {
    data object OnBackClicked : LoginPasswordIntent
    data class OnEmailChanged(val email: String) : LoginPasswordIntent
    data class OnPasswordChanged(val password: String) : LoginPasswordIntent
    data object OnLoginClicked : LoginPasswordIntent
}

sealed interface LoginPasswordSideEffect {
    data object NavigateBack : LoginPasswordSideEffect
    data object NavigateToClient : LoginPasswordSideEffect
    data object NavigateToMerchant : LoginPasswordSideEffect
    data object NavigateToPartner : LoginPasswordSideEffect
    data class ShowError(val message: String) : LoginPasswordSideEffect
}
