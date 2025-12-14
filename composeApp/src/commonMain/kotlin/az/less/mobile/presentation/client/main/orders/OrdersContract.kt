package az.less.mobile.presentation.client.main.orders

import az.less.mobile.presentation.client.main.orders.models.CartItem

/**
 * State of the Orders Screen
 */
data class OrdersState(
    val selectedTab: az.less.mobile.presentation.client.main.orders.OrderTab = _root_ide_package_.az.less.mobile.presentation.client.main.orders.OrderTab.CART,
    val cartItems: List<az.less.mobile.presentation.client.main.orders.models.CartItem> = emptyList(),
    val historyItems: List<az.less.mobile.presentation.client.main.orders.models.CartItem> = emptyList(),
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
    data object NavigateToCheckout : az.less.mobile.presentation.client.main.orders.OrdersSideEffect
    data class ShowReserveInfo(val cartItem: az.less.mobile.presentation.client.main.orders.models.CartItem) :
        az.less.mobile.presentation.client.main.orders.OrdersSideEffect
    data object NavigateBack : az.less.mobile.presentation.client.main.orders.OrdersSideEffect
    data class ShowError(val message: String) :
        az.less.mobile.presentation.client.main.orders.OrdersSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface OrdersIntent {
    data class OnTabSelected(val tab: az.less.mobile.presentation.client.main.orders.OrderTab) :
        az.less.mobile.presentation.client.main.orders.OrdersIntent
    data class OnCartItemClicked(val itemId: String) :
        az.less.mobile.presentation.client.main.orders.OrdersIntent
    data object OnCheckoutClicked : az.less.mobile.presentation.client.main.orders.OrdersIntent
    data object OnBackClicked : az.less.mobile.presentation.client.main.orders.OrdersIntent
}

