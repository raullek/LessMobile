package az.less.mobile.presentation.client.account.paymentmethods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class PaymentMethodsViewModel : ViewModel(), ContainerHost<PaymentMethodsState, PaymentMethodsSideEffect> {
    
    override val container: Container<PaymentMethodsState, PaymentMethodsSideEffect> = 
        viewModelScope.container(PaymentMethodsState())

    fun onIntent(intent: PaymentMethodsIntent) {
        when (intent) {
            is PaymentMethodsIntent.OnBackClicked -> handleBackClicked()
            is PaymentMethodsIntent.OnCardSelected -> handleCardSelected(intent.cardId)
            is PaymentMethodsIntent.OnDeleteCardClicked -> handleDeleteCardClicked(intent.cardId)
            is PaymentMethodsIntent.OnConfirmDelete -> handleConfirmDelete()
            is PaymentMethodsIntent.OnCancelDelete -> handleCancelDelete()
            is PaymentMethodsIntent.OnAddNewCardClicked -> handleAddNewCardClicked()
            is PaymentMethodsIntent.OnApplePayClicked -> handleApplePayClicked()
            is PaymentMethodsIntent.OnGooglePayClicked -> handleGooglePayClicked()
        }
    }
    
    private fun handleBackClicked() = intent {
        postSideEffect(PaymentMethodsSideEffect.NavigateBack)
    }
    
    private fun handleCardSelected(cardId: String) = intent {
        reduce {
            val updatedCards = state.creditDebitCards.map { card ->
                card.copy(isSelected = card.id == cardId)
            }
            state.copy(creditDebitCards = updatedCards)
        }
    }
    
    private fun handleDeleteCardClicked(cardId: String) = intent {
        val cardToDelete = state.creditDebitCards.find { it.id == cardId }
        reduce {
            state.copy(cardToDelete = cardToDelete)
        }
    }
    
    private fun handleConfirmDelete() = intent {
        val cardId = state.cardToDelete?.id
        if (cardId != null) {
            reduce {
                val updatedCards = state.creditDebitCards.filter { it.id != cardId }
                state.copy(
                    creditDebitCards = updatedCards,
                    cardToDelete = null
                )
            }
        }
    }
    
    private fun handleCancelDelete() = intent {
        reduce {
            state.copy(cardToDelete = null)
        }
    }
    
    private fun handleAddNewCardClicked() = intent {
        postSideEffect(PaymentMethodsSideEffect.NavigateToAddCard)
    }
    
    private fun handleApplePayClicked() = intent {
        postSideEffect(PaymentMethodsSideEffect.NavigateToApplePay)
    }
    
    private fun handleGooglePayClicked() = intent {
        postSideEffect(PaymentMethodsSideEffect.NavigateToGooglePay)
    }
}



