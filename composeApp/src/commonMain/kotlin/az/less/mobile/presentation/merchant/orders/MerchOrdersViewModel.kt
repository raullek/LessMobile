package az.less.mobile.presentation.merchant.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.OrdersRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.presentation.merchant.orders.model.BoughtBoxItem
import az.less.mobile.presentation.merchant.orders.model.CreatedBoxItem
import az.less.mobile.presentation.merchant.orders.model.MerchOrderTab
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class MerchOrdersViewModel(
    private val ordersRepository: OrdersRepository,
    private val sessionLocalRepository: SessionLocalRepository
) : ViewModel(), ContainerHost<MerchOrdersState, MerchOrdersSideEffect> {

    override val container: Container<MerchOrdersState, MerchOrdersSideEffect> =
        viewModelScope.container(MerchOrdersState())

    init {
        loadOrders()
    }

    fun onIntent(intent: MerchOrdersIntent) {
        when (intent) {
            is MerchOrdersIntent.OnTabSelected -> handleTabSelected(intent.tab)
            is MerchOrdersIntent.OnHandedOverClicked -> handleHandedOver(intent.orderId)
            is MerchOrdersIntent.OnCancelLotClicked -> handleCancelLot(intent.boxId)
            is MerchOrdersIntent.OnOrderClicked -> handleOrderClicked(intent.orderId)
            is MerchOrdersIntent.OnRefresh -> refreshOrders()
        }
    }

    private fun loadOrders() = intent {
        reduce { state.copy(isLoading = true, error = null) }
        fetchOrders()
    }

    private fun refreshOrders() = intent {
        reduce { state.copy(isRefreshing = true, error = null) }
        fetchOrders()
    }

    private suspend fun fetchOrders() {
        val user = sessionLocalRepository.currentUser.first()
        val venueId = user?.venue?.id

        if (venueId == null) {
            intent {
                reduce { state.copy(isLoading = false, isRefreshing = false) }
            }
            return
        }

        ordersRepository.getMerchantOrders(venueId)
            .onSuccess { data ->
                val boughtBoxes = data.boughtBoxes.data.map { dto ->
                    BoughtBoxItem(
                        id = dto.id,
                        orderId = dto.orderId,
                        reserveNumber = dto.reserveNumber,
                        boxTitle = dto.box.title,
                        imageUrl = dto.box.images.firstOrNull(),
                        originalPrice = dto.box.originalPrice,
                        discountedPrice = dto.box.discountedPrice,
                        clientName = dto.client.name,
                        clientAvatar = dto.client.avatar,
                        quantity = dto.quantity,
                        subtotal = dto.subtotal,
                        status = dto.status,
                        pickupTimeFormatted = dto.pickupTimeFormatted ?: "",
                        canDeliver = dto.canDeliver
                    )
                }

                val createdBoxes = data.createdBoxes.data.map { dto ->
                    CreatedBoxItem(
                        id = dto.id,
                        title = dto.title,
                        description = dto.description,
                        imageUrl = dto.images.firstOrNull(),
                        originalPrice = dto.originalPrice,
                        discountedPrice = dto.discountedPrice,
                        quantity = dto.quantity,
                        soldCount = dto.soldCount,
                        availableItems = dto.availableItems,
                        pickupTimeFormatted = dto.pickupTimeFormatted ?: "",
                        status = dto.status,
                        isActive = dto.isActive,
                        canCancel = dto.canCancel,
                        timeRemaining = dto.timeRemaining,
                        timeRemainingSeconds = dto.timeRemainingSeconds,
                        cancellationExpired = dto.cancellationExpired,
                        closeTimeFormatted = dto.closeTimeFormatted,
                        cancellationMessage = dto.cancellationMessage,
                        cancelButtonText = dto.cancelButtonText
                    )
                }

                intent {
                    reduce {
                        state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            boughtBoxes = boughtBoxes,
                            createdBoxes = createdBoxes,
                            boughtTitle = data.boughtBoxes.title,
                            createdTitle = data.createdBoxes.title
                        )
                    }
                }
            }
            .onError { error ->
                intent {
                    reduce { state.copy(isLoading = false, isRefreshing = false) }
                    postSideEffect(MerchOrdersSideEffect.ShowError(error.message))
                }
            }
    }

    private fun handleTabSelected(tab: MerchOrderTab) = intent {
        reduce { state.copy(selectedTab = tab) }
    }

    private fun handleHandedOver(orderId: String) = intent {
        // TODO: Call API to mark order as handed over
        postSideEffect(MerchOrdersSideEffect.OrderHandedOverSuccess)
        reduce {
            state.copy(
                boughtBoxes = state.boughtBoxes.filter { it.id != orderId }
            )
        }
    }

    private fun handleCancelLot(boxId: String) = intent {
        // TODO: Call API to cancel the lot
        postSideEffect(MerchOrdersSideEffect.OrderCancelledSuccess)
        reduce {
            state.copy(
                createdBoxes = state.createdBoxes.filter { it.id != boxId }
            )
        }
    }

    private fun handleOrderClicked(orderId: String) = intent {
        postSideEffect(MerchOrdersSideEffect.ShowOrderDetails(orderId))
    }
}
