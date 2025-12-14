package az.less.mobile.presentation.client.onboarding.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Login Code Screen using Orbit MVI
 */
class LoginCodeViewModel : ViewModel(), ContainerHost<LoginCodeState, LoginCodeSideEffect> {

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

        // TODO: Call API to verify code
        // Simulate API call - on error, clear code and show error
        // For now, simulate verification failure for testing
        reduce {
            state.copy(isLoading = false)
        }
        
        // Simulate error for testing - remove this when real API is implemented
        // Uncomment below to test error state:
        // reduce {
        //     state.copy(
        //         codeError = "Invalid code. Please try again.",
        //         code = "" // Clear input on error
        //     )
        // }
        // return@intent
        
        postSideEffect(LoginCodeSideEffect.NavigateNext)
    }

    private fun handleResendCodeClicked() = intent {
        // TODO: Call API to resend code
        postSideEffect(LoginCodeSideEffect.ShowSuccess("Code resent successfully"))
    }
}

