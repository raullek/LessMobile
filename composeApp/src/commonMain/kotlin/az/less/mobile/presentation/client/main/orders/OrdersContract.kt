package az.less.mobile.presentation.client.main.orders

import az.less.mobile.presentation.client.main.orders.models.Order

/**
 * State of the Orders Screen
 */
data class OrdersState(
    val selectedTab: OrderTab = OrderTab.ACTIVE,
    val isLoggedIn: Boolean = true
)

/**
 * Order tabs enum
 */
enum class OrderTab {
    ACTIVE,
    PREVIOUS
}

/**
 * Side Effects for navigation and one-time events
 */
sealed interface OrdersSideEffect {
    data object NavigateToCheckout : OrdersSideEffect
    data class ShowReserveInfo(val order: Order) : OrdersSideEffect
    data object NavigateBack : OrdersSideEffect
    data class ShowError(val message: String) : OrdersSideEffect
    data object NavigateToOffers : OrdersSideEffect
    data object NavigateToMore : OrdersSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface OrdersIntent {
    data class OnTabSelected(val tab: OrderTab) : OrdersIntent
    data class OnOrderClicked(val order: Order) : OrdersIntent
    data object OnCheckoutClicked : OrdersIntent
    data object OnBackClicked : OrdersIntent
    data object OnExploreOffersClicked : OrdersIntent
    data object OnSignInClicked : OrdersIntent
}
