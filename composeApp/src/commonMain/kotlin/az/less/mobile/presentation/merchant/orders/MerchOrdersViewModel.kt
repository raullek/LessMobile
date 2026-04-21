package az.less.mobile.presentation.merchant.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.data.remote.model.BoughtBoxDto
import az.less.mobile.data.remote.model.CreatedBoxDto
import az.less.mobile.domain.repository.OrdersRepository
import az.less.mobile.presentation.merchant.orders.model.BoughtBoxItem
import az.less.mobile.presentation.merchant.orders.model.CreatedBoxItem
import az.less.mobile.presentation.merchant.orders.model.MerchOrderTab
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class MerchOrdersViewModel(
    private val ordersRepository: OrdersRepository
) : ViewModel(), ContainerHost<MerchOrdersState, MerchOrdersSideEffect> {

    override val container: Container<MerchOrdersState, MerchOrdersSideEffect> =
        viewModelScope.container(MerchOrdersState())

    private var sseJob: Job? = null
    private var timerJob: Job? = null

    init {
        loadOrders()
        // Default tab is AWAITING_PICKUP → start SSE
        startBoughtBoxesStream()
    }

    fun onIntent(intent: MerchOrdersIntent) {
        when (intent) {
            is MerchOrdersIntent.OnTabSelected -> handleTabSelected(intent.tab)
            is MerchOrdersIntent.OnHandedOverClicked -> handleHandedOver(intent.orderId)
            is MerchOrdersIntent.OnCancelLotClicked -> handleCancelLot(intent.boxId)
            is MerchOrdersIntent.OnOrderClicked -> handleOrderClicked(intent.orderId)
            is MerchOrdersIntent.OnRefresh -> refreshOrders()
            is MerchOrdersIntent.OnLotAdded -> handleLotAdded()
        }
    }

    private fun handleLotAdded() = intent {
        val tabChanged = state.selectedTab != MerchOrderTab.AWAITING_PURCHASE
        reduce {
            state.copy(
                selectedTab = MerchOrderTab.AWAITING_PURCHASE,
                createdBoxes = emptyList(),
                isCreatedBoxesReloading = true
            )
        }
        if (tabChanged) activateTab(MerchOrderTab.AWAITING_PURCHASE)

        ordersRepository.getCreatedBoxes()
            .onSuccess { data ->
                intent {
                    reduce {
                        state.copy(
                            createdBoxes = data.data.map { it.toCreatedBoxItem() },
                            createdTitle = data.title?.ifEmpty { state.createdTitle } ?: state.createdTitle,
                            isCreatedBoxesReloading = false
                        )
                    }
                    postSideEffect(MerchOrdersSideEffect.ScrollCreatedBoxesToTop)
                }
            }
            .onError { error ->
                intent {
                    reduce { state.copy(isCreatedBoxesReloading = false) }
                    postSideEffect(MerchOrdersSideEffect.ShowError(error.message))
                }
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
        // Fetch both endpoints in parallel
        var boughtResult: List<BoughtBoxItem>? = null
        var boughtTitle = ""
        var createdResult: List<CreatedBoxItem>? = null
        var createdTitle = ""
        var hasError = false
        var errorMessage = ""

        val boughtJob = viewModelScope.launch {
            ordersRepository.getBoughtBoxes()
                .onSuccess { data ->
                    boughtTitle = data.title ?: ""
                    boughtResult = data.data.map { it.toBoughtBoxItem() }
                }
                .onError { error ->
                    hasError = true
                    errorMessage = error.message
                }
        }

        val createdJob = viewModelScope.launch {
            ordersRepository.getCreatedBoxes()
                .onSuccess { data ->
                    createdTitle = data.title ?: ""
                    createdResult = data.data.map { it.toCreatedBoxItem() }
                }
                .onError { error ->
                    hasError = true
                    errorMessage = error.message
                }
        }

        boughtJob.join()
        createdJob.join()

        intent {
            if (hasError && boughtResult == null && createdResult == null) {
                reduce { state.copy(isLoading = false, isRefreshing = false) }
                postSideEffect(MerchOrdersSideEffect.ShowError(errorMessage))
            } else {
                reduce {
                    state.copy(
                        isLoading = false,
                        isRefreshing = false,
                        boughtBoxes = boughtResult ?: state.boughtBoxes,
                        createdBoxes = createdResult ?: state.createdBoxes,
                        boughtTitle = boughtTitle.ifEmpty { state.boughtTitle },
                        createdTitle = createdTitle.ifEmpty { state.createdTitle }
                    )
                }
                // Start background job for the active tab
                activateTab(state.selectedTab)
            }
        }
    }

    /**
     * Subscribe to SSE stream for real-time bought boxes updates.
     * Auto-retries on failure with exponential backoff.
     */
    private fun startBoughtBoxesStream() {
        if (sseJob?.isActive == true) return
        sseJob = viewModelScope.launch {
            ordersRepository.streamBoughtBoxes()
                .retry(Long.MAX_VALUE) { cause ->
                    kotlinx.coroutines.delay(5000)
                    true
                }
                .catch { /* Stream ended or failed after retries */ }
                .collect { data ->
                    intent {
                        reduce {
                            state.copy(
                                boughtBoxes = data.data.map { it.toBoughtBoxItem() },
                                boughtTitle = data.title?.ifEmpty { state.boughtTitle } ?: state.boughtTitle
                            )
                        }
                    }
                }
        }
    }

    /**
     * Ticks every second, decrements timeRemainingSeconds for cancellable boxes.
     * When a box reaches 0, it flips to cancellationExpired state.
     * Stops when no active timers remain.
     */
    private fun startCancellationTimer() {
        if (timerJob?.isActive == true) return
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                intent {
                    val hasActiveTimers = state.createdBoxes.any { it.isCancelEnabled }
                    if (!hasActiveTimers) {
                        timerJob?.cancel()
                        return@intent
                    }

                    reduce {
                        state.copy(
                            createdBoxes = state.createdBoxes.map { box ->
                                if (!box.isCancelEnabled) return@map box

                                val newSeconds = (box.timeRemainingSeconds - 1).coerceAtLeast(0)
                                if (newSeconds <= 0) {
                                    // Timer expired
                                    box.copy(
                                        timeRemainingSeconds = 0,
                                        cancellationExpired = true,
                                        cancelButtonText = null
                                    )
                                } else {
                                    // Tick: update seconds and button text
                                    val minutes = newSeconds / 60
                                    val secs = newSeconds % 60
                                    val mm = if (minutes < 10) "0$minutes" else "$minutes"
                                    val ss = if (secs < 10) "0$secs" else "$secs"
                                    val timeFormatted = "$mm:$ss"
                                    box.copy(
                                        timeRemainingSeconds = newSeconds,
                                        timeRemaining = timeFormatted,
                                        cancelButtonText = box.cancelButtonText?.let {
                                            it.replace(Regex("\\d{2}:\\d{2}"), timeFormatted)
                                        }
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun handleTabSelected(tab: MerchOrderTab) = intent {
        val tabChanged = state.selectedTab != tab
        if (tabChanged) {
            reduce { state.copy(selectedTab = tab) }
            activateTab(tab)
        }
        fetchTabData(tab)
    }

    private suspend fun fetchTabData(tab: MerchOrderTab) {
        when (tab) {
            MerchOrderTab.AWAITING_PICKUP -> {
                ordersRepository.getBoughtBoxes()
                    .onSuccess { data ->
                        intent {
                            reduce {
                                state.copy(
                                    boughtBoxes = data.data.map { it.toBoughtBoxItem() },
                                    boughtTitle = data.title?.ifEmpty { state.boughtTitle } ?: state.boughtTitle
                                )
                            }
                        }
                    }
                    .onError { error ->
                        intent { postSideEffect(MerchOrdersSideEffect.ShowError(error.message)) }
                    }
            }
            MerchOrderTab.AWAITING_PURCHASE -> {
                ordersRepository.getCreatedBoxes()
                    .onSuccess { data ->
                        intent {
                            reduce {
                                state.copy(
                                    createdBoxes = data.data.map { it.toCreatedBoxItem() },
                                    createdTitle = data.title?.ifEmpty { state.createdTitle } ?: state.createdTitle
                                )
                            }
                        }
                    }
                    .onError { error ->
                        intent { postSideEffect(MerchOrdersSideEffect.ShowError(error.message)) }
                    }
            }
        }
    }

    /**
     * Starts the background job for the active tab, stops the other.
     * AWAITING_PICKUP → SSE on, timer off
     * AWAITING_PURCHASE → SSE off, timer on
     */
    private fun activateTab(tab: MerchOrderTab) {
        when (tab) {
            MerchOrderTab.AWAITING_PICKUP -> {
                stopCancellationTimer()
                startBoughtBoxesStream()
            }
            MerchOrderTab.AWAITING_PURCHASE -> {
                stopBoughtBoxesStream()
                startCancellationTimer()
            }
        }
    }

    private fun stopBoughtBoxesStream() {
        sseJob?.cancel()
        sseJob = null
    }

    private fun stopCancellationTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun handleHandedOver(orderId: String) = intent {
        // Find the item to get the actual order ID
        val item = state.boughtBoxes.find { it.id == orderId } ?: return@intent
        reduce { state.copy(deliveringOrderId = orderId) }

        ordersRepository.deliverOrder(item.orderId)
            .onSuccess {
                reduce {
                    state.copy(
                        deliveringOrderId = null,
                        boughtBoxes = state.boughtBoxes.filter { it.id != orderId }
                    )
                }
                postSideEffect(MerchOrdersSideEffect.OrderHandedOverSuccess)
            }
            .onError { error ->
                reduce { state.copy(deliveringOrderId = null) }
                postSideEffect(MerchOrdersSideEffect.ShowError(error.message))
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

    override fun onCleared() {
        sseJob?.cancel()
        timerJob?.cancel()
        super.onCleared()
    }
}

private fun BoughtBoxDto.toBoughtBoxItem() = BoughtBoxItem(
    id = id,
    orderId = orderId ?: id,
    reserveNumber = reserveNumber ?: "",
    boxTitle = box?.title ?: box?.defaultBoxTitle ?: "",
    boxType = box?.boxType,
    imageUrl = box?.image ?: box?.defaultBoxImage,
    originalPrice = box?.originalPrice ?: 0.0,
    discountedPrice = box?.discountedPrice ?: 0.0,
    clientName = client?.name ?: "",
    clientAvatar = client?.avatar,
    quantity = quantity ?: 1,
    subtotal = subtotal ?: 0.0,
    status = status ?: "",
    pickupTimeFormatted = pickupTimeFormatted ?: "",
    canDeliver = canDeliver ?: false
)

private fun CreatedBoxDto.toCreatedBoxItem() = CreatedBoxItem(
    id = id,
    title = title ?: defaultBoxTitle ?: "",
    boxType = boxType,
    description = description,
    imageUrl = image ?: defaultBoxImage,
    originalPrice = originalPrice ?: 0.0,
    discountedPrice = discountedPrice ?: 0.0,
    quantity = quantity ?: 0,
    soldCount = soldCount ?: 0,
    availableItems = availableItems ?: 0,
    pickupTimeFormatted = pickupTimeFormatted ?: "",
    status = status,
    isActive = isActive ?: true,
    canCancel = canCancel ?: false,
    timeRemaining = timeRemaining,
    timeRemainingSeconds = timeRemainingSeconds ?: 0,
    cancellationExpired = cancellationExpired ?: false,
    closeTimeFormatted = closeTimeFormatted,
    cancellationMessage = cancellationMessage,
    cancelButtonText = cancelButtonText
)
