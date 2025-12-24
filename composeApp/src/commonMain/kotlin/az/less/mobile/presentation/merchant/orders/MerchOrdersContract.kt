package az.less.mobile.presentation.merchant.orders

import az.less.mobile.presentation.merchant.orders.model.MerchOrderItem
import az.less.mobile.presentation.merchant.orders.model.MerchOrderTab

/**
 * State of the Merchant Orders Screen
 */
data class MerchOrdersState(
    val selectedTab: MerchOrderTab = MerchOrderTab.AWAITING_PICKUP,
    val awaitingPickupOrders: List<MerchOrderItem> = emptyList(),
    val awaitingPurchaseOrders: List<MerchOrderItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface MerchOrdersSideEffect {
    data class ShowError(val message: String) : MerchOrdersSideEffect
    data class ShowOrderDetails(val orderId: String) : MerchOrdersSideEffect
    data object OrderHandedOverSuccess : MerchOrdersSideEffect
    data object OrderCancelledSuccess : MerchOrdersSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface MerchOrdersIntent {
    /** User selected a tab */
    data class OnTabSelected(val tab: MerchOrderTab) : MerchOrdersIntent
    
    /** User clicked "Handed Over" button */
    data class OnHandedOverClicked(val orderId: String) : MerchOrdersIntent
    
    /** User clicked "Cancel Lot" button */
    data class OnCancelLotClicked(val orderId: String) : MerchOrdersIntent
    
    /** User clicked on an order card */
    data class OnOrderClicked(val orderId: String) : MerchOrdersIntent
    
    /** Pull to refresh */
    data object OnRefresh : MerchOrdersIntent
}

