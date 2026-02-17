package az.less.mobile.presentation.client.onboarding.loginemail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.AuthorizationRepository
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Login Email Screen using Orbit MVI
 */
class LoginEmailViewModel(
    private val authorizationRepository: AuthorizationRepository
) : ViewModel(), ContainerHost<LoginEmailState, LoginEmailSideEffect> {

    override val container: Container<LoginEmailState, LoginEmailSideEffect> =
        viewModelScope.container(LoginEmailState())

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
        reduce { state.copy(email = email, emailError = null) }
    }

    private fun handleNextClicked() = intent {
        if (state.isEmailValid) {
            reduce { state.copy(isLoading = true, emailError = null) }

            authorizationRepository.emailLogin(state.email)
                .onSuccess {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(LoginEmailSideEffect.NavigateNext)
                }
                .onError { emailError ->
                    reduce { state.copy(isLoading = false, emailError = emailError.message) }
                }
        }
    }
}
