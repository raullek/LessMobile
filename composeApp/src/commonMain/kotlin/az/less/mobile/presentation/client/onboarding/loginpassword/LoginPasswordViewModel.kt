package az.less.mobile.presentation.client.onboarding.loginpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.model.auth.AppMode
import az.less.mobile.domain.repository.AuthorizationRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import kotlinx.coroutines.flow.first
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class LoginPasswordViewModel(
    private val sessionLocalRepository: SessionLocalRepository,
    private val authorizationRepository: AuthorizationRepository
) : ViewModel(), ContainerHost<LoginPasswordState, LoginPasswordSideEffect> {

    override val container: Container<LoginPasswordState, LoginPasswordSideEffect> =
        viewModelScope.container(LoginPasswordState())

    fun onIntent(intent: LoginPasswordIntent) {
        when (intent) {
            is LoginPasswordIntent.OnBackClicked -> handleBackClicked()
            is LoginPasswordIntent.OnEmailChanged -> handleEmailChanged(intent.email)
            is LoginPasswordIntent.OnPasswordChanged -> handlePasswordChanged(intent.password)
            is LoginPasswordIntent.OnLoginClicked -> handleLoginClicked()
        }
    }

    private fun handleBackClicked() = intent {
        postSideEffect(LoginPasswordSideEffect.NavigateBack)
    }

    private fun handleEmailChanged(email: String) = intent {
        reduce { state.copy(email = email, emailError = null) }
    }

    private fun handlePasswordChanged(password: String) = intent {
        reduce { state.copy(password = password, passwordError = null) }
    }

    private fun handleLoginClicked() = intent {
        if (state.isFormValid) {
            reduce { state.copy(isLoading = true, emailError = null, passwordError = null) }

            authorizationRepository.loginWithPassword(state.email, state.password)
                .onSuccess {
                    reduce { state.copy(isLoading = false) }

                    val user = sessionLocalRepository.currentUser.first()
                    val lastMode = sessionLocalRepository.lastUsedMode.first()
                    val target = user?.resolveStartMode(lastMode) ?: AppMode.CLIENT
                    sessionLocalRepository.saveLastUsedMode(target)
                    when (target) {
                        AppMode.MERCHANT -> postSideEffect(LoginPasswordSideEffect.NavigateToMerchant)
                        AppMode.PARTNER -> postSideEffect(LoginPasswordSideEffect.NavigateToPartner)
                        AppMode.CLIENT -> postSideEffect(LoginPasswordSideEffect.NavigateToClient)
                    }
                }
                .onError { error ->
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(LoginPasswordSideEffect.ShowError(error.message))
                }
        }
    }
}
