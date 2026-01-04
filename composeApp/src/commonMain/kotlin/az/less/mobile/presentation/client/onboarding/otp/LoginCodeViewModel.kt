package az.less.mobile.presentation.client.onboarding.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.data.model.User
import az.less.mobile.data.repository.UserRepository
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import kotlin.random.Random

/**
 * ViewModel for Login Code Screen using Orbit MVI
 */
class LoginCodeViewModel(
    private val userRepository: UserRepository
) : ViewModel(), ContainerHost<LoginCodeState, LoginCodeSideEffect> {

    override val container: Container<LoginCodeState, LoginCodeSideEffect> =
        viewModelScope.container(LoginCodeState())

    /**
     * Handle user intents
     */
    fun onIntent(intent: LoginCodeIntent) {
        when (intent) {
            is LoginCodeIntent.OnBackClicked -> handleBackClicked()
            is LoginCodeIntent.OnCodeChanged -> handleCodeChanged(intent.code)
            is LoginCodeIntent.OnVerifyClicked -> handleVerifyClicked()
            is LoginCodeIntent.OnResendCodeClicked -> handleResendCodeClicked()
        }
    }

    private fun handleBackClicked() = intent {
        postSideEffect(LoginCodeSideEffect.NavigateBack)
    }

    private fun handleCodeChanged(code: String) = intent {
        // Only allow digits, limit to 6 digits
        val digitsOnly = code.filter { it.isDigit() }.take(6)
        
        reduce {
            state.copy(
                code = digitsOnly,
                codeError = null // Clear error when user makes changes
            )
        }
        
        // Auto-verify when all digits are entered
        if (digitsOnly.length == 6) {
            handleVerifyClicked()
        }
    }

    private fun handleVerifyClicked() = intent {
        if (state.code.length != 6) {
            reduce {
                state.copy(
                    codeError = "Please enter 6-digit code",
                    code = "" // Clear input on error
                )
            }
            return@intent
        }

        reduce {
            state.copy(isLoading = true, codeError = null)
        }

        // Mock API verification - only "111111" is valid
        if (state.code != VALID_CODE) {
            reduce {
                state.copy(
                    isLoading = false,
                    codeError = "Invalid code. Please try again.",
                    code = "" // Clear input on error
                )
            }
            return@intent
        }

        // Create mock user and save to DataStore
        val mockUser = User(
            id = "user_${Random.nextInt(100000, 999999)}",
            name = "Maqa",
            email = state.email.ifEmpty { "maqa@gmail.com" },
            avatarUrl = null,
            co2Saved = "60 kg",
            moneySaved = "$120"
        )

        userRepository.saveUser(mockUser)

        reduce {
            state.copy(isLoading = false)
        }

        postSideEffect(LoginCodeSideEffect.NavigateNext)
    }

    companion object {
        private const val VALID_CODE = "111111"
    }

    private fun handleResendCodeClicked() = intent {
        // TODO: Call API to resend code
        postSideEffect(LoginCodeSideEffect.ShowSuccess("Code resent successfully"))
    }
}

