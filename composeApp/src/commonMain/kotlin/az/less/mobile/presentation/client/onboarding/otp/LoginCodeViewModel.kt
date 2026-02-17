package az.less.mobile.presentation.client.onboarding.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.data.model.User
import az.less.mobile.data.repository.UserRepository
import az.less.mobile.domain.repository.AuthorizationRepository
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import kotlin.random.Random

class LoginCodeViewModel(
    private val userRepository: UserRepository,
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
                    if (data.verified) {
                        val user = User(
                            id = "user_${Random.nextInt(100000, 999999)}",
                            name = "User",
                            email = state.email,
                            avatarUrl = null,
                            co2Saved = "0 kg",
                            moneySaved = "$0"
                        )
                        userRepository.saveUser(user)
                        reduce { state.copy(isLoading = false) }
                        postSideEffect(LoginCodeSideEffect.NavigateNext)
                    } else {
                        reduce { state.copy(isLoading = false, codeError = "Verification failed", code = "") }
                    }
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
