package az.less.mobile.presentation.client.onboarding.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.model.auth.AppMode
import az.less.mobile.domain.repository.AuthorizationRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import kotlinx.coroutines.flow.first
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class LoginCodeViewModel(
    private val sessionLocalRepository: SessionLocalRepository,
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
                    reduce { state.copy(isLoading = false) }

                    val user = sessionLocalRepository.currentUser.first()
                    val lastMode = sessionLocalRepository.lastUsedMode.first()
                    val available = user?.availableModes().orEmpty()
                    val target = when {
                        lastMode == AppMode.MERCHANT && AppMode.MERCHANT in available -> AppMode.MERCHANT
                        lastMode == AppMode.PARTNER && AppMode.PARTNER in available -> AppMode.PARTNER
                        else -> AppMode.CLIENT
                    }
                    sessionLocalRepository.saveLastUsedMode(target)
                    when (target) {
                        AppMode.MERCHANT -> postSideEffect(LoginCodeSideEffect.NavigateToMerchant)
                        AppMode.PARTNER -> postSideEffect(LoginCodeSideEffect.NavigateToPartner)
                        AppMode.CLIENT -> postSideEffect(LoginCodeSideEffect.NavigateToClient)
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
