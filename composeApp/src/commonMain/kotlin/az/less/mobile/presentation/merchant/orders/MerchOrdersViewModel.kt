package az.less.mobile.presentation.merchant.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.merchant.orders.model.MerchOrderItem
import az.less.mobile.presentation.merchant.orders.model.MerchOrderTab
import az.less.mobile.presentation.merchant.orders.model.OrderButtonState
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Merchant Orders Screen using Orbit MVI
 */
class MerchOrdersViewModel : ViewModel(), ContainerHost<MerchOrdersState, MerchOrdersSideEffect> {

    override val container: Container<MerchOrdersState, MerchOrdersSideEffect> =
        viewModelScope.container(MerchOrdersState())

    init {
        loadOrders()
    }

    /**
     * Handle user intents
     */
    fun onIntent(intent: MerchOrdersIntent) {
        when (intent) {
            is MerchOrdersIntent.OnTabSelected -> handleTabSelected(intent.tab)
            is MerchOrdersIntent.OnHandedOverClicked -> handleHandedOver(intent.orderId)
            is MerchOrdersIntent.OnCancelLotClicked -> handleCancelLot(intent.orderId)
            is MerchOrdersIntent.OnOrderClicked -> handleOrderClicked(intent.orderId)
            is MerchOrdersIntent.OnRefresh -> loadOrders()
        }
    }

    private fun loadOrders() = intent {
        reduce { state.copy(isLoading = true, error = null) }
        
        // TODO: Replace with actual repository call
        val awaitingPickup = getMockAwaitingPickupOrders()
        val awaitingPurchase = getMockAwaitingPurchaseOrders()
        
        reduce {
            state.copy(
                isLoading = false,
                awaitingPickupOrders = awaitingPickup,
                awaitingPurchaseOrders = awaitingPurchase
            )
        }
    }

    private fun handleTabSelected(tab: MerchOrderTab) = intent {
        reduce { state.copy(selectedTab = tab) }
    }

    private fun handleHandedOver(orderId: String) = intent {
        // TODO: Call API to mark order as handed over
        // For now, just show success message
        postSideEffect(MerchOrdersSideEffect.OrderHandedOverSuccess)
        
        // Remove the order from the list
        reduce {
            state.copy(
                awaitingPickupOrders = state.awaitingPickupOrders.filter { it.id != orderId }
            )
        }
    }

    private fun handleCancelLot(orderId: String) = intent {
        // TODO: Call API to cancel the order
        postSideEffect(MerchOrdersSideEffect.OrderCancelledSuccess)
        
        // Remove the order from the list
        reduce {
            state.copy(
                awaitingPurchaseOrders = state.awaitingPurchaseOrders.filter { it.id != orderId }
            )
        }
    }

    private fun handleOrderClicked(orderId: String) = intent {
        postSideEffect(MerchOrdersSideEffect.ShowOrderDetails(orderId))
    }

    // Mock data - replace with repository calls in real app
    private fun getMockAwaitingPickupOrders(): List<MerchOrderItem> {
        return listOf(
            MerchOrderItem(
                id = "1",
                merchantName = "Belgian Chocolate & Coffee",
                productName = "Small Surprise Bag",
                originalPrice = "22.99",
                discountedPrice = "12.99",
                pickupTimeStart = "17:00",
                pickupTimeEnd = "23:00",
                orderNumber = "#4535",
                rating = "4.9",
                distance = "1.2 km",
                itemsLeft = 12,
                buttonState = OrderButtonState.HANDED_OVER
            ),
            MerchOrderItem(
                id = "2",
                merchantName = "Belgian Chocolate & Coffee",
                productName = "Small Surprise Bag",
                originalPrice = "22.99",
                discountedPrice = "12.99",
                pickupTimeStart = "17:00",
                pickupTimeEnd = "23:00",
                orderNumber = "#4535",
                rating = "4.9",
                distance = "1.2 km",
                itemsLeft = 12,
                buttonState = OrderButtonState.HANDED_OVER
            ),
            MerchOrderItem(
                id = "3",
                merchantName = "Belgian Chocolate & Coffee",
                productName = "Small Surprise Bag",
                originalPrice = "22.99",
                discountedPrice = "12.99",
                pickupTimeStart = "17:00",
                pickupTimeEnd = "23:00",
                orderNumber = "#4535",
                rating = "4.9",
                distance = "1.2 km",
                itemsLeft = 12,
                buttonState = OrderButtonState.HANDED_OVER
            )
        )
    }

    private fun getMockAwaitingPurchaseOrders(): List<MerchOrderItem> {
        return listOf(
            MerchOrderItem(
                id = "4",
                merchantName = "Belgian Chocolate & Coffee",
                productName = "Medium Surprise Bag",
                originalPrice = "35.99",
                discountedPrice = "19.99",
                pickupTimeStart = "10:00",
                pickupTimeEnd = "18:00",
                orderNumber = "#4536",
                rating = "4.9",
                distance = "0.8 km",
                itemsLeft = 5,
                buttonState = OrderButtonState.CANCEL_LOT
            ),
            MerchOrderItem(
                id = "5",
                merchantName = "Belgian Chocolate & Coffee",
                productName = "Large Surprise Bag",
                originalPrice = "45.99",
                discountedPrice = "25.99",
                pickupTimeStart = "12:00",
                pickupTimeEnd = "20:00",
                orderNumber = "#4537",
                rating = "4.8",
                distance = "2.1 km",
                itemsLeft = 3,
                buttonState = OrderButtonState.CANCELLATION_TIME_ENDED
            )
        )
    }
}

