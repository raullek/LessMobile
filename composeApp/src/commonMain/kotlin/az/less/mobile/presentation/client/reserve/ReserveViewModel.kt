package az.less.mobile.presentation.client.reserve

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.client.reserve.models.CardType
import az.less.mobile.presentation.client.reserve.models.OrderAccepted
import az.less.mobile.presentation.client.reserve.models.PaymentCard
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import kotlin.random.Random

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
            is ReserveIntent.OnPaymentCardSelected -> handlePaymentCardSelected(intent.card)
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
        
        // Mock payment cards data
        val mockPaymentCards = listOf(
            PaymentCard(
                id = "card_1",
                type = CardType.MASTERCARD,
                lastFourDigits = "2412",
                isSelected = true
            ),
            PaymentCard(
                id = "card_2",
                type = CardType.VISA,
                lastFourDigits = "3440",
                isSelected = false
            ),
            PaymentCard(
                id = "add_new",
                type = CardType.ADD_NEW,
                lastFourDigits = "",
                isSelected = false
            )
        )
        
        val selectedCard = mockPaymentCards.firstOrNull { it.isSelected }
        
        reduce {
            state.copy(
                venueName = "Small Surprise Bag",
                pickupTime = "Pick up from 17:00 to 23:00",
                quantity = quantity,
                itemsLeft = 8,
                pricePerPiece = pricePerPiece,
                serviceFee = serviceFee,
                subtotal = subtotal,
                availablePaymentCards = mockPaymentCards,
                selectedPaymentCard = selectedCard,
                paymentMethodDisplay = selectedCard?.let {
                    when (it.type) {
                        CardType.MASTERCARD -> "Mastercard •••• ${it.lastFourDigits}"
                        CardType.VISA -> "Visa •••• ${it.lastFourDigits}"
                        CardType.ADD_NEW -> ""
                    }
                } ?: ""
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
        
        // Generate order number (6 digits)
        val orderNumber = Random.nextInt(100000, 999999).toString()
        
        // Create order info
        val orderInfo = OrderAccepted(
            orderNumber = orderNumber,
            venueName = state.venueName,
            pickupTime = state.pickupTime
        )
        
        // Place order and navigate to success screen
        postSideEffect(ReserveSideEffect.OrderPlaced(orderInfo))
    }

    private fun handlePaymentMethodClicked() = intent {
        postSideEffect(ReserveSideEffect.NavigateToPaymentMethods)
    }

    private fun handlePaymentCardSelected(card: PaymentCard) = intent {
        // Handle "Add new card" separately (TODO: navigate to add card screen)
        if (card.type == CardType.ADD_NEW) {
            // TODO: Navigate to add new card screen
            return@intent
        }
        
        // Update selected card and payment method display
        val updatedCards = state.availablePaymentCards.map {
            it.copy(isSelected = it.id == card.id)
        }
        
        val displayText = when (card.type) {
            CardType.MASTERCARD -> "Mastercard •••• ${card.lastFourDigits}"
            CardType.VISA -> "Visa •••• ${card.lastFourDigits}"
            CardType.ADD_NEW -> ""
        }
        
        reduce {
            state.copy(
                selectedPaymentCard = card,
                availablePaymentCards = updatedCards,
                paymentMethodDisplay = displayText
            )
        }
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

