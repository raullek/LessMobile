package az.less.mobile.presentation.client.main.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import az.less.mobile.domain.repository.OrdersRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.presentation.client.main.orders.models.Order
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Orders Screen using Orbit MVI
 */
@OptIn(ExperimentalCoroutinesApi::class)
class OrdersViewModel(
    private val ordersRepository: OrdersRepository,
    private val sessionLocalRepository: SessionLocalRepository
) : ViewModel(), ContainerHost<OrdersState, OrdersSideEffect> {

    override val container: Container<OrdersState, OrdersSideEffect> =
        viewModelScope.container(OrdersState())

    private val activeType = MutableStateFlow<String?>(null)
    private val previousType = MutableStateFlow<String?>(null)

    val activeOrders: Flow<PagingData<Order>> = activeType
        .flatMapLatest { type ->
            if (type == null) {
                flowOf(PagingData.empty())
            } else {
                ordersRepository.getOrders(type)
            }
        }
        .cachedIn(viewModelScope)

    val previousOrders: Flow<PagingData<Order>> = previousType
        .flatMapLatest { type ->
            if (type == null) {
                flowOf(PagingData.empty())
            } else {
                ordersRepository.getOrders(type)
            }
        }
        .cachedIn(viewModelScope)

    init {
        observeSession()
    }

    /**
     * Handle user intents
     */
    fun onIntent(intent: OrdersIntent) {
        when (intent) {
            is OrdersIntent.OnTabSelected -> handleTabSelected(intent.tab)
            is OrdersIntent.OnOrderClicked -> handleOrderClicked(intent.order)
            is OrdersIntent.OnCheckoutClicked -> handleCheckoutClicked()
            is OrdersIntent.OnBackClicked -> handleBackClicked()
            is OrdersIntent.OnExploreOffersClicked -> handleExploreOffersClicked()
            is OrdersIntent.OnSignInClicked -> handleSignInClicked()
        }
    }

    private fun observeSession() {
        viewModelScope.launch {
            sessionLocalRepository.isLoggedIn.collect { loggedIn ->
                intent { reduce { state.copy(isLoggedIn = loggedIn) } }
                if (loggedIn) {
                    activeType.value = "active"
                    previousType.value = "previous"
                }
            }
        }
    }

    private fun handleTabSelected(tab: OrderTab) = intent {
        reduce { state.copy(selectedTab = tab) }
    }

    private fun handleOrderClicked(order: Order) = intent {
        postSideEffect(OrdersSideEffect.ShowReserveInfo(order))
    }

    private fun handleCheckoutClicked() = intent {
        postSideEffect(OrdersSideEffect.NavigateToCheckout)
    }

    private fun handleBackClicked() = intent {
        postSideEffect(OrdersSideEffect.NavigateBack)
    }

    private fun handleExploreOffersClicked() = intent {
        postSideEffect(OrdersSideEffect.NavigateToOffers)
    }

    private fun handleSignInClicked() = intent {
        postSideEffect(OrdersSideEffect.NavigateToMore)
    }
}
