package az.less.mobile.presentation.client.onboarding.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.model.auth.User
import az.less.mobile.domain.repository.AuthorizationRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class LoginCodeViewModel(
    private val userLocalRepository: SessionLocalRepository,
    private val authorizationRepository: AuthorizationRepository
) : ViewModel(), ContainerHost<LoginCodeState, LoginCodeSideEffect> {

    override val container: Container<LoginCodeState, LoginCodeSideEffect> =
        viewModelScope.container(LoginCodeState())

    fun setEmail(email: String) = intent {
        reduce { state.copy(email = email) }
    }

    fun onIntent(intent: LoginCodeIntent) {
        when (intent) {
            is LoginCodeIntent.OnBackClicked -> handleBackClicked()
            is LoginCodeIntent.OnCodeChanged -> handleCodeChanged(intent.code)
            is LoginCodeIntent.OnVerifyClicked -> verifyOtp()
            is LoginCodeIntent.OnResendCodeClicked -> handleResendCodeClicked()
        }
    }

    private fun handleBackClicked() = intent {
        postSideEffect(LoginCodeSideEffect.NavigateBack)
    }

    private fun handleCodeChanged(code: String) = intent {
        val digitsOnly = code.filter { it.isDigit() }.take(6)
        reduce { state.copy(code = digitsOnly, codeError = null) }
    }

    private fun verifyOtp() = intent {
        if (state.isCodeValid) {
            reduce { state.copy(isLoading = true, codeError = null) }

            authorizationRepository.verifyOtp(state.email, state.code)
                .onSuccess { data ->
                    val user = User(
                        id = data.user.id,
                        name = data.user.name,
                        email = data.user.email,
                        roles = data.user.roles,
                        status = data.user.status
                    )
                    userLocalRepository.saveSession(
                        user = user,
                        accessToken = data.accessToken,
                        refreshToken = data.refreshToken
                    )
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(LoginCodeSideEffect.NavigateNext)
                }
                .onError { error ->
                    reduce { state.copy(isLoading = false, codeError = error.message, code = "") }
                }
        }
    }

    private fun handleResendCodeClicked() = intent {
        reduce { state.copy(isLoading = true) }

        authorizationRepository.resendOtp(state.email)
            .onSuccess { data ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(LoginCodeSideEffect.ShowSuccess(data.message))
            }
            .onError { error ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(LoginCodeSideEffect.ShowError(error.message))
            }
    }
}
