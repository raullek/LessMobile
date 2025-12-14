package az.less.mobile.presentation.client.onboarding.loginemail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Login Email Screen using Orbit MVI
 */
class LoginEmailViewModel : ViewModel(), ContainerHost<LoginEmailState, LoginEmailSideEffect> {

    override val container: Container<LoginEmailState, LoginEmailSideEffect> =
        viewModelScope.container(LoginEmailState())

    /**
     * Handle user intents
     */
    fun onIntent(intent: LoginEmailIntent) {
        when (intent) {
            is LoginEmailIntent.OnBackClicked -> handleBackClicked()
            is LoginEmailIntent.OnEmailChanged -> handleEmailChanged(intent.email)
            is LoginEmailIntent.OnNextClicked -> handleNextClicked()
        }
    }

    private fun handleBackClicked() = intent {
        postSideEffect(LoginEmailSideEffect.NavigateBack)
    }

    private fun handleEmailChanged(email: String) = intent {
        reduce {
            state.copy(
                email = email,
                emailError = null // Clear error when user makes changes
            )
        }
    }

    private fun handleNextClicked() = intent {
        val emailError = validateEmail(state.email)

        reduce {
            state.copy(emailError = emailError)
        }

        // If email is valid, proceed to next screen
        if (emailError == null && state.email.isNotEmpty()) {
            postSideEffect(LoginEmailSideEffect.NavigateNext)
        } else if (state.email.isEmpty()) {
            postSideEffect(LoginEmailSideEffect.ShowError("Please enter your email"))
        }
    }

    private fun validateEmail(email: String): String? {
        if (email.isEmpty()) return null

        return when {
            !email.contains("@") -> "Email must contain @"
            !email.contains(".") -> "Email must contain a domain"
            email.length < 5 -> "Email is too short"
            else -> null
        }
    }
}

