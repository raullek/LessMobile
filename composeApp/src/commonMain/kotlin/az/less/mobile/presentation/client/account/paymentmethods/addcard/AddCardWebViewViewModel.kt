package az.less.mobile.presentation.client.account.paymentmethods.addcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.OffersRepository
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class AddCardWebViewViewModel(
    private val offersRepository: OffersRepository
) : ViewModel(), ContainerHost<AddCardWebViewState, AddCardWebViewSideEffect> {

    override val container: Container<AddCardWebViewState, AddCardWebViewSideEffect> =
        viewModelScope.container(AddCardWebViewState())

    fun onIntent(intent: AddCardWebViewIntent) {
        when (intent) {
            is AddCardWebViewIntent.OnCallbackUrlDetected -> handleCallbackUrl(intent.callbackUrl)
            is AddCardWebViewIntent.OnErrorCallbackDetected -> handleErrorCallback()
            is AddCardWebViewIntent.OnBackClicked -> {}
        }
    }

    private fun handleCallbackUrl(callbackUrl: String) = intent {
        if (state.isVerifying) return@intent

        reduce { state.copy(isVerifying = true) }

        offersRepository.verifyCard(callbackUrl)
            .onSuccess {
                reduce { state.copy(isVerifying = false, showSuccessToast = true) }
                postSideEffect(AddCardWebViewSideEffect.CardAddedSuccessfully)
            }
            .onError { error ->
                reduce { state.copy(isVerifying = false, showErrorToast = true) }
                postSideEffect(AddCardWebViewSideEffect.CardAdditionFailed)
            }
    }

    private fun handleErrorCallback() = intent {
        reduce { state.copy(showErrorToast = true) }
        postSideEffect(AddCardWebViewSideEffect.CardAdditionFailed)
    }
}
