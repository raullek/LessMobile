package az.less.mobile.presentation.client.account.paymentmethods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.OffersRepository
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class PaymentMethodsViewModel(
    private val offersRepository: OffersRepository
) : ViewModel(), ContainerHost<PaymentMethodsState, PaymentMethodsSideEffect> {

    override val container: Container<PaymentMethodsState, PaymentMethodsSideEffect> =
        viewModelScope.container(PaymentMethodsState())

    init {
        loadCards()
    }

    fun onIntent(intent: PaymentMethodsIntent) {
        when (intent) {
            is PaymentMethodsIntent.OnBackClicked -> handleBackClicked()
            is PaymentMethodsIntent.OnCardSelected -> handleCardSelected(intent.cardId)
            is PaymentMethodsIntent.OnDeleteCardClicked -> handleDeleteCardClicked(intent.cardId)
            is PaymentMethodsIntent.OnConfirmDelete -> handleConfirmDelete()
            is PaymentMethodsIntent.OnCancelDelete -> handleCancelDelete()
            is PaymentMethodsIntent.OnAddNewCardClicked -> handleAddNewCardClicked()
            is PaymentMethodsIntent.LoadCards -> loadCards()
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

    private fun loadCards() = intent {
        reduce { state.copy(isLoading = true) }

        offersRepository.getPaymentMethods()
            .onSuccess { methods ->
                val cards = methods.cards.map { card ->
                    val cardType = when (card.brand?.lowercase()) {
                        "visa" -> PaymentMethodType.VISA
                        "mastercard" -> PaymentMethodType.MASTERCARD
                        else -> PaymentMethodType.VISA
                    }
                    PaymentMethod(
                        id = card.id,
                        type = cardType,
                        lastFourDigits = card.last4 ?: card.cardMask ?: "",
                        isSelected = card.isDefault
                    )
                }
                reduce {
                    state.copy(
                        isLoading = false,
                        creditDebitCards = cards
                    )
                }
            }
            .onError {
                reduce { state.copy(isLoading = false) }
            }
    }

    private fun handleAddNewCardClicked() = intent {
        reduce { state.copy(isRegisterCardLoading = true) }

        offersRepository.registerCard()
            .onSuccess { response ->
                reduce { state.copy(isRegisterCardLoading = false) }
                val url = response.redirectUrl ?: response.url
                if (url != null) {
                    postSideEffect(PaymentMethodsSideEffect.OpenAddCardWebView(url))
                }
            }
            .onError { error ->
                reduce { state.copy(isRegisterCardLoading = false) }
                postSideEffect(PaymentMethodsSideEffect.ShowError(error.message))
            }
    }

}
