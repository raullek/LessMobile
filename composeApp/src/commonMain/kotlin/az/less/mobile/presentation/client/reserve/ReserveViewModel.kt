package az.less.mobile.presentation.client.reserve

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.data.remote.model.VoucherItemDto
import az.less.mobile.domain.model.VoucherValidation
import az.less.mobile.domain.repository.OffersRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.domain.repository.VouchersRepository
import az.less.mobile.domain.usecase.CalculateOrderPriceUseCase
import az.less.mobile.domain.usecase.ValidateVoucherUseCase
import az.less.mobile.presentation.client.reserve.models.CardType
import az.less.mobile.presentation.client.reserve.models.OrderAccepted
import az.less.mobile.presentation.client.reserve.models.PaymentCard
import az.less.mobile.presentation.client.reserve.models.Voucher
import az.less.mobile.presentation.client.reserve.models.toPaymentCard
import kotlinx.coroutines.flow.firstOrNull
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class ReserveViewModel(
    private val offersRepository: OffersRepository,
    private val vouchersRepository: VouchersRepository,
    private val sessionLocalRepository: SessionLocalRepository,
    private val calculateOrderPrice: CalculateOrderPriceUseCase,
    private val validateVoucher: ValidateVoucherUseCase
) : ViewModel(), ContainerHost<ReserveState, ReserveSideEffect> {

    override val container: Container<ReserveState, ReserveSideEffect> =
        viewModelScope.container(ReserveState())

    private var currentBoxId: String? = null

    fun onIntent(intent: ReserveIntent) {
        when (intent) {
            is ReserveIntent.Initialize -> handleInitialize(intent.boxId)
            is ReserveIntent.OnBackClicked -> handleBackClicked()
            is ReserveIntent.OnPlaceOrderClicked -> handlePlaceOrderClicked()
            is ReserveIntent.OnPaymentMethodClicked -> handlePaymentMethodClicked()
            is ReserveIntent.OnPaymentCardSelected -> handlePaymentCardSelected(intent.card)
            is ReserveIntent.OnIncrementQuantity -> handleIncrementQuantity()
            is ReserveIntent.OnDecrementQuantity -> handleDecrementQuantity()
            is ReserveIntent.OnSeeMoreDealsClicked -> handleSeeMoreDealsClicked()
            is ReserveIntent.OnLotInfoClicked -> handleLotInfoClicked()
            is ReserveIntent.OnLotInfoDismissed -> handleLotInfoDismissed()
            is ReserveIntent.OnSelectVoucherClicked -> handleSelectVoucherClicked()
            is ReserveIntent.OnVoucherSelected -> handleVoucherSelected(intent.voucher)
            is ReserveIntent.OnVoucherRemoved -> handleVoucherRemoved()
            is ReserveIntent.OnVoucherBottomSheetDismissed -> handleVoucherBottomSheetDismissed()
            is ReserveIntent.OnAddNewCardClicked -> handleAddNewCardClicked()
            is ReserveIntent.OnPaymentSheetDismissed -> handlePaymentSheetDismissed()
        }
    }

    // --- Data loading ---

    private fun handleInitialize(boxId: String) {
        if (boxId == currentBoxId) return
        currentBoxId = boxId
        loadBoxDetail(boxId)
    }

    private fun loadBoxDetail(boxId: String) = intent {
        reduce { state.copy(isLoading = true, error = null, boxId = boxId) }

        val feeRate = sessionLocalRepository.currentUser.firstOrNull()
            ?.appDefaults?.serviceeFeeRate ?: 0.0

        offersRepository.getBoxDetail(boxId)
            .onSuccess { detail ->
                val quantity = 1
                val prices = calculateOrderPrice(quantity, detail.discountedPrice, null, feeRate)

                val mockLotSizeInfo = listOf(
                    LotSizeInfo(name = "Small", description = "1-2 persons"),
                    LotSizeInfo(name = "Medium", description = "3-4 persons"),
                    LotSizeInfo(name = "Large", description = "5-6 persons")
                )

                reduce {
                    state.copy(
                        isLoading = false,
                        lotName = detail.title,
                        pickupTime = detail.pickupTimeFormatted,
                        lotDescription = detail.description,
                        quantity = quantity,
                        itemsLeft = detail.availableQuantity,
                        pricePerPiece = detail.discountedPrice,
                        originalPrice = detail.originalPrice,
                        serviceFeeRate = feeRate,
                        serviceFee = prices.serviceFee,
                        subtotal = prices.subtotal,
                        subtotalBeforeDiscount = prices.boxTotal,
                        venueName = detail.venue.businessName,
                        venueId = detail.venue.id,
                        venueLogoUrl = detail.venue.businessLogo,
                        lotSizeInfoList = mockLotSizeInfo
                    )
                }

                loadDefaultPayment()
                loadVouchers()
            }
            .onError { error ->
                reduce { state.copy(isLoading = false, error = error.message) }
                postSideEffect(ReserveSideEffect.ShowError(error.message))
            }
    }

    private fun loadDefaultPayment() = intent {
        reduce { state.copy(isPaymentLoading = true) }
        offersRepository.getDefaultPayment()
            .onSuccess { payment ->
                val card = payment.toPaymentCard(isSelected = true)
                reduce {
                    state.copy(
                        isPaymentLoading = false,
                        selectedPaymentCard = card,
                        paymentMethodDisplay = payment.displayName
                    )
                }
            }
            .onError { reduce { state.copy(isPaymentLoading = false) } }
    }

    private fun loadVouchers() = intent {
        vouchersRepository.getVouchers(page = 1, limit = 20)
            .onSuccess { data ->
                val vouchers = data.data.map { it.toReserveVoucher() }
                reduce { state.copy(availableVouchers = vouchers) }
            }
            .onError { reduce { state.copy(availableVouchers = emptyList()) } }
    }

    // --- Quantity ---

    private fun handleIncrementQuantity() = intent {
        if (state.quantity < state.itemsLeft) {
            reduce { recalculate(state, state.quantity + 1) }
        }
    }

    private fun handleDecrementQuantity() = intent {
        if (state.quantity > 1) {
            reduce { recalculate(state, state.quantity - 1) }
        }
    }

    /**
     * Recalculate prices for new quantity. If voucher becomes invalid — remove it + set warning.
     */
    private fun recalculate(currentState: ReserveState, newQuantity: Int): ReserveState {
        val voucher = currentState.selectedVoucher

        if (voucher != null) {
            val validation = validateVoucher(voucher, newQuantity, currentState.pricePerPiece)
            if (validation is VoucherValidation.Invalid) {
                val prices = calculateOrderPrice(newQuantity, currentState.pricePerPiece, null, currentState.serviceFeeRate)
                return currentState.copy(
                    quantity = newQuantity,
                    selectedVoucher = null,
                    discount = 0.0,
                    serviceFee = prices.serviceFee,
                    subtotal = prices.subtotal,
                    subtotalBeforeDiscount = prices.boxTotal,
                    voucherWarning = "Voucher removed: ${validation.reason}"
                )
            }
        }

        val prices = calculateOrderPrice(newQuantity, currentState.pricePerPiece, voucher, currentState.serviceFeeRate)
        return currentState.copy(
            quantity = newQuantity,
            discount = prices.discount,
            serviceFee = prices.serviceFee,
            subtotal = prices.subtotal,
            subtotalBeforeDiscount = prices.boxTotal,
            voucherWarning = null
        )
    }

    // --- Voucher ---

    private fun handleSelectVoucherClicked() = intent {
        reduce { state.copy(isVoucherBottomSheetVisible = true, voucherWarning = null) }
    }

    private fun handleVoucherSelected(voucher: Voucher) = intent {
        val validation = validateVoucher(voucher, state.quantity, state.pricePerPiece)

        if (validation is VoucherValidation.Invalid) {
            reduce {
                state.copy(
                    isVoucherBottomSheetVisible = false,
                    voucherWarning = validation.reason
                )
            }
            return@intent
        }

        val updatedVouchers = state.availableVouchers.map { it.copy(isSelected = it.id == voucher.id) }
        val prices = calculateOrderPrice(state.quantity, state.pricePerPiece, voucher, state.serviceFeeRate)
        reduce {
            state.copy(
                selectedVoucher = voucher,
                availableVouchers = updatedVouchers,
                discount = prices.discount,
                serviceFee = prices.serviceFee,
                subtotal = prices.subtotal,
                subtotalBeforeDiscount = prices.boxTotal,
                isVoucherBottomSheetVisible = false,
                voucherWarning = null
            )
        }
    }

    private fun handleVoucherRemoved() = intent {
        val updatedVouchers = state.availableVouchers.map { it.copy(isSelected = false) }
        val prices = calculateOrderPrice(state.quantity, state.pricePerPiece, null, state.serviceFeeRate)
        reduce {
            state.copy(
                selectedVoucher = null,
                availableVouchers = updatedVouchers,
                discount = 0.0,
                serviceFee = prices.serviceFee,
                subtotal = prices.subtotal,
                subtotalBeforeDiscount = prices.boxTotal,
                voucherWarning = null
            )
        }
    }

    private fun handleVoucherBottomSheetDismissed() = intent {
        reduce { state.copy(isVoucherBottomSheetVisible = false) }
    }

    // --- Payment ---

    private fun handlePaymentMethodClicked() = intent {
        reduce { state.copy(isPaymentSheetVisible = true, isPaymentCardsLoading = true) }
        loadAllPaymentCards()
    }

    private fun loadAllPaymentCards() = intent {
        offersRepository.getPaymentMethods()
            .onSuccess { methods ->
                val cards = methods.map { it.toPaymentCard() }
                reduce { state.copy(isPaymentCardsLoading = false, availablePaymentCards = cards) }
            }
            .onError { reduce { state.copy(isPaymentCardsLoading = false) } }
    }

    private fun handlePaymentSheetDismissed() = intent {
        reduce { state.copy(isPaymentSheetVisible = false) }
    }

    private fun handlePaymentCardSelected(card: PaymentCard) = intent {
        if (card.type == CardType.ADD_NEW) return@intent

        val updatedCards = state.availablePaymentCards.map { it.copy(isSelected = it.id == card.id) }
        val displayText = card.displayName.ifEmpty {
            when (card.type) {
                CardType.MASTERCARD -> "Mastercard •••• ${card.lastFourDigits}"
                CardType.VISA -> "Visa •••• ${card.lastFourDigits}"
                CardType.ADD_NEW -> ""
            }
        }
        reduce {
            state.copy(
                selectedPaymentCard = card,
                availablePaymentCards = updatedCards,
                paymentMethodDisplay = displayText,
                isPaymentSheetVisible = false
            )
        }
    }

    private fun handleAddNewCardClicked() = intent {
        reduce { state.copy(isRegisterCardLoading = true) }
        offersRepository.registerCard()
            .onSuccess { response ->
                reduce { state.copy(isRegisterCardLoading = false) }
                val url = response.redirectUrl ?: response.url
                if (url != null) postSideEffect(ReserveSideEffect.OpenRedirectUrl(url))
            }
            .onError { error ->
                reduce { state.copy(isRegisterCardLoading = false) }
                postSideEffect(ReserveSideEffect.ShowError(error.message))
            }
    }

    // --- Order ---

    private fun handlePlaceOrderClicked() = intent {
        val paymentCard = state.selectedPaymentCard
        if (paymentCard == null) {
            postSideEffect(ReserveSideEffect.ShowError("Please select a payment method"))
            return@intent
        }

        reduce { state.copy(isLoading = true) }
        offersRepository.placeOrder(
            boxId = state.boxId,
            quantity = state.quantity,
            paymentMethodId = paymentCard.id,
            userVoucherId = state.selectedVoucher?.id
        )
            .onSuccess { data ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(
                    ReserveSideEffect.OrderPlaced(
                        OrderAccepted(
                            orderNumber = data.order?.reserveNumber ?: "",
                            venueName = state.venueName,
                            pickupTime = state.pickupTime
                        )
                    )
                )
            }
            .onError { error ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(ReserveSideEffect.ShowError(error.message))
            }
    }

    // --- Navigation ---

    private fun handleBackClicked() = intent {
        postSideEffect(ReserveSideEffect.NavigateBack)
    }

    private fun handleSeeMoreDealsClicked() = intent {
        postSideEffect(ReserveSideEffect.NavigateToMerchant(state.venueId))
    }

    private fun handleLotInfoClicked() = intent {
        reduce { state.copy(isLotSizeInfoVisible = true) }
    }

    private fun handleLotInfoDismissed() = intent {
        reduce { state.copy(isLotSizeInfoVisible = false) }
    }
}

private fun VoucherItemDto.toReserveVoucher(): Voucher {
    return Voucher(
        id = id,
        name = snapshot?.title ?: "Voucher",
        discountAmount = snapshot?.value ?: 0.0,
        minSubtotal = snapshot?.minSubtotal,
        maxDiscount = snapshot?.maxDiscount,
        expiresInDays = 0
    )
}
