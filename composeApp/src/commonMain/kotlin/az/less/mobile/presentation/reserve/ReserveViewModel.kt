package az.less.mobile.presentation.reserve

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Reserve Screen using Orbit MVI
 */
class ReserveViewModel : ViewModel(), ContainerHost<ReserveState, ReserveSideEffect> {

    override val container: Container<ReserveState, ReserveSideEffect> =
        viewModelScope.container(ReserveState())

    init {
        loadInitialData()
    }

    /**
     * Handle user intents
     */
    fun onIntent(intent: ReserveIntent) {
        when (intent) {
            is ReserveIntent.OnBackClicked -> handleBackClicked()
            is ReserveIntent.OnPlaceOrderClicked -> handlePlaceOrderClicked()
            is ReserveIntent.OnPaymentMethodClicked -> handlePaymentMethodClicked()
            is ReserveIntent.OnIncrementQuantity -> handleIncrementQuantity()
            is ReserveIntent.OnDecrementQuantity -> handleDecrementQuantity()
            is ReserveIntent.OnSeeMoreDealsClicked -> handleSeeMoreDealsClicked()
        }
    }

    private fun loadInitialData() = intent {
        val quantity = 2
        val pricePerPiece = 12.55
        val serviceFee = 0.20
        val subtotal = calculateSubtotal(quantity, pricePerPiece, serviceFee)
        
        reduce {
            state.copy(
                venueName = "Small Surprise Bag",
                pickupTime = "Pick up from 17:00 to 23:00",
                quantity = quantity,
                itemsLeft = 8,
                pricePerPiece = pricePerPiece,
                serviceFee = serviceFee,
                subtotal = subtotal,
                paymentMethodDisplay = ""
            )
        }
    }
    
    private fun calculateSubtotal(quantity: Int, pricePerPiece: Double, serviceFee: Double): Double {
        return (quantity * pricePerPiece) + serviceFee
    }

    private fun handleBackClicked() = intent {
        postSideEffect(ReserveSideEffect.NavigateBack)
    }

    private fun handlePlaceOrderClicked() = intent {
        if (state.paymentMethodDisplay.isEmpty()) {
            postSideEffect(ReserveSideEffect.ShowError("Please select a payment method"))
            return@intent
        }
        
        // Place order logic here
        postSideEffect(ReserveSideEffect.OrderPlaced)
    }

    private fun handlePaymentMethodClicked() = intent {
        postSideEffect(ReserveSideEffect.NavigateToPaymentMethods)
    }

    private fun handleIncrementQuantity() = intent {
        if (state.quantity < state.itemsLeft) {
            val newQuantity = state.quantity + 1
            reduce {
                state.copy(
                    quantity = newQuantity,
                    subtotal = calculateSubtotal(newQuantity, state.pricePerPiece, state.serviceFee)
                )
            }
        }
    }

    private fun handleDecrementQuantity() = intent {
        if (state.quantity > 1) {
            val newQuantity = state.quantity - 1
            reduce {
                state.copy(
                    quantity = newQuantity,
                    subtotal = calculateSubtotal(newQuantity, state.pricePerPiece, state.serviceFee)
                )
            }
        }
    }

    private fun handleSeeMoreDealsClicked() = intent {
        // Navigate to see more deals
    }
}

