package az.less.mobile.presentation.client.main.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.client.main.orders.models.CartItem
import kotlinx.coroutines.delay
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
        reduce { state.copy(isLoading = true) }

        // Mock network delay for shimmer loading demonstration
        delay(2000L)

        reduce {
            state.copy(
                cartItems = getMockCartItems(),
                historyItems = getMockHistoryItems(),
                isLoading = false
            )
        }
    }

    private fun handleTabSelected(tab: OrderTab) =
        intent {
           reduce {
               state.copy(selectedTab = tab)
            }
        }

    private fun handleCartItemClicked(itemId: String) =
        intent {
            // Find the cart item and show reserve info
            val cartItem =
               state.cartItems.find { it.id == itemId }
                    ?:state.historyItems.find { it.id == itemId }

            if (cartItem != null) {
               postSideEffect(
                    OrdersSideEffect.ShowReserveInfo(
                        cartItem
                    )
                )
            }
        }

    private fun handleCheckoutClicked() =
        intent {
            if (state.cartItems.isNotEmpty()) {
               postSideEffect(
                    OrdersSideEffect.NavigateToCheckout
                )
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
                title = "Small Surprise Bag",
                pickupTime = "Pick up from 17:00 to 23:00",
                price = "12.99",
                reserveNumber = "234529"
            ),
            CartItem(
                id = "2",
                title = "Mixed donut bag",
                pickupTime = "Pick up from 14:00 to 18:00",
                price = "8.50",
                reserveNumber = "234530"
            )
        )
    }

    private fun getMockHistoryItems(): List<CartItem> {
        return listOf(
            CartItem(
                id = "h1",
                title = "Small Surprise Bag",
                pickupTime = "Pick up from 17:00 to 23:00",
                price = "12.99",
                reserveNumber = "234529",
                isCompleted = true,
                completedDate = "12 November"
            ),
            CartItem(
                id = "h2",
                title = "Mixed donut bag",
                pickupTime = "Pick up from 14:00 to 18:00",
                price = "8.50",
                reserveNumber = "234528",
                isCompleted = true,
                completedDate = "10 November"
            ),
            CartItem(
                id = "h3",
                title = "Coffee & Pastry Set",
                pickupTime = "Pick up from 09:00 to 12:00",
                price = "15.00",
                reserveNumber = "234527",
                isCompleted = true,
                completedDate = "5 November"
            )
        )
    }
}

