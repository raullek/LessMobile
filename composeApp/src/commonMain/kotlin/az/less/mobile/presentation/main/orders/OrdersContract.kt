package az.less.mobile.presentation.main.orders

import az.less.mobile.presentation.main.orders.models.CartItem

/**
 * State of the Orders Screen
 */
data class OrdersState(
    val selectedTab: OrderTab = OrderTab.CART,
    val cartItems: List<CartItem> = emptyList(),
    val historyItems: List<CartItem> = emptyList(),
    val isLoading: Boolean = false
)

/**
 * Order tabs enum
 */
enum class OrderTab {
    CART,
    HISTORY
}

/**
 * Side Effects for navigation and one-time events
 */
sealed interface OrdersSideEffect {
    data object NavigateToCheckout : OrdersSideEffect
    data class NavigateToItemDetail(val itemId: String) : OrdersSideEffect
    data object NavigateBack : OrdersSideEffect
    data class ShowError(val message: String) : OrdersSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface OrdersIntent {
    data class OnTabSelected(val tab: OrderTab) : OrdersIntent
    data class OnCartItemClicked(val itemId: String) : OrdersIntent
    data object OnCheckoutClicked : OrdersIntent
    data object OnBackClicked : OrdersIntent
}

