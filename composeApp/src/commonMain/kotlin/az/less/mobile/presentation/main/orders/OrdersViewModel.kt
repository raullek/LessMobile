package az.less.mobile.presentation.main.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.main.orders.models.CartItem
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Orders Screen using Orbit MVI
 */
class OrdersViewModel : ViewModel(), ContainerHost<OrdersState, OrdersSideEffect> {

    override val container: Container<OrdersState, OrdersSideEffect> =
        viewModelScope.container(OrdersState())

    init {
        loadCartItems()
    }

    /**
     * Handle user intents
     */
    fun onIntent(intent: OrdersIntent) {
        when (intent) {
            is OrdersIntent.OnTabSelected -> handleTabSelected(intent.tab)
            is OrdersIntent.OnCartItemClicked -> handleCartItemClicked(intent.itemId)
            is OrdersIntent.OnCheckoutClicked -> handleCheckoutClicked()
            is OrdersIntent.OnBackClicked -> handleBackClicked()
        }
    }

    private fun loadCartItems() = intent {
        reduce {
            state.copy(
                cartItems = getMockCartItems()
            )
        }
    }

    private fun handleTabSelected(tab: OrderTab) = intent {
        reduce {
            state.copy(selectedTab = tab)
        }
    }

    private fun handleCartItemClicked(itemId: String) = intent {
        postSideEffect(OrdersSideEffect.NavigateToItemDetail(itemId))
    }

    private fun handleCheckoutClicked() = intent {
        if (state.cartItems.isNotEmpty()) {
            postSideEffect(OrdersSideEffect.NavigateToCheckout)
        }
    }

    private fun handleBackClicked() = intent {
        postSideEffect(OrdersSideEffect.NavigateBack)
    }

    // Mock data - replace with repository calls in real app
    private fun getMockCartItems(): List<CartItem> {
        return listOf(
            CartItem(
                id = "1",
                title = "Mixed donut bag",
                pickupTime = "Pick up from 17:00 to 23:00",
                price = "12.99"
            ),
            CartItem(
                id = "2",
                title = "Mixed donut bag",
                pickupTime = "Pick up from 17:00 to 23:00",
                price = "12.99"
            ),
            CartItem(
                id = "3",
                title = "Mixed donut bag",
                pickupTime = "Pick up from 17:00 to 23:00",
                price = "12.99"
            ),
            CartItem(
                id = "4",
                title = "Mixed donut bag",
                pickupTime = "Pick up from 17:00 to 23:00",
                price = "12.99"
            ),
            CartItem(
                id = "5",
                title = "Mixed donut bag",
                pickupTime = "Pick up from 17:00 to 23:00",
                price = "12.99"
            ),
            CartItem(
                id = "6",
                title = "Mixed donut bag",
                pickupTime = "Pick up from 17:00 to 23:00",
                price = "12.99"
            ),
            CartItem(
                id = "7",
                title = "Mixed donut bag",
                pickupTime = "Pick up from 17:00 to 23:00",
                price = "12.99"
            ),
            CartItem(
                id = "8",
                title = "Mixed donut bag",
                pickupTime = "Pick up from 17:00 to 23:00",
                price = "12.99"
            )
        )
    }
}

