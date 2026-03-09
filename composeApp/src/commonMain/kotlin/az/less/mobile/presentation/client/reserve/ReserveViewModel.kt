package az.less.mobile.presentation.client.reserve

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.data.remote.model.DefaultPaymentDto
import az.less.mobile.data.remote.model.PaymentCardDto
import az.less.mobile.domain.repository.OffersRepository
import az.less.mobile.presentation.client.reserve.models.CardType
import az.less.mobile.presentation.client.reserve.models.OrderAccepted
import az.less.mobile.presentation.client.reserve.models.PaymentCard
import az.less.mobile.presentation.client.reserve.models.Voucher
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import az.less.mobile.utils.roundPrice
import kotlin.random.Random

/**
 * ViewModel for Reserve Screen using Orbit MVI
 */
class ReserveViewModel(
    private val offersRepository: OffersRepository
) : ViewModel(), ContainerHost<ReserveState, ReserveSideEffect> {

    override val container: Container<ReserveState, ReserveSideEffect> =
        viewModelScope.container(ReserveState())

    private var currentBoxId: String? = null

    /**
     * Handle user intents
     */
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
            is ReserveIntent.OnVoucherBottomSheetDismissed -> handleVoucherBottomSheetDismissed()
            is ReserveIntent.OnAddNewCardClicked -> handleAddNewCardClicked()
            is ReserveIntent.OnPaymentSheetDismissed -> handlePaymentSheetDismissed()
        }
    }

    private fun handleInitialize(boxId: String) {
        if (boxId == currentBoxId) return
        currentBoxId = boxId
        loadBoxDetail(boxId)
    }

    private fun loadBoxDetail(boxId: String) = intent {
        reduce { state.copy(isLoading = true, error = null, boxId = boxId) }

        offersRepository.getBoxDetail(boxId)
            .onSuccess { detail ->
                val quantity = 1
                val subtotal = calculateSubtotal(quantity, detail.discountedPrice)

                // Mock lot size info
                val mockLotSizeInfo = listOf(
                    LotSizeInfo(name = "Small", description = "1-2 persons"),
                    LotSizeInfo(name = "Medium", description = "3-4 persons"),
                    LotSizeInfo(name = "Large", description = "5-6 persons")
                )

                // Mock vouchers
                val mockVouchers = listOf(
                    Voucher(
                        id = "voucher_1",
                        name = "2 AZN Voucher",
                        discountAmount = 2.0,
                        expiresInDays = 7,
                        isSelected = false
                    ),
                    Voucher(
                        id = "voucher_2",
                        name = "6 AZN Voucher",
                        discountAmount = 6.0,
                        expiresInDays = 7,
                        isSelected = false
                    )
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
                        subtotal = subtotal,
                        venueName = detail.venue.businessName,
                        venueId = detail.venue.id,
                        venueLogoUrl = detail.venue.businessLogo,
                        lotSizeInfoList = mockLotSizeInfo,
                        availableVouchers = mockVouchers
                    )
                }

                // Load default payment method
                loadDefaultPayment()
            }
            .onError { error ->
                reduce {
                    state.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
                postSideEffect(ReserveSideEffect.ShowError(error.message))
            }
    }

    private fun loadDefaultPayment() = intent {
        reduce { state.copy(isPaymentLoading = true) }

        offersRepository.getDefaultPayment()
            .onSuccess { payment ->
                val card = payment.toPaymentCard()
                reduce {
                    state.copy(
                        isPaymentLoading = false,
                        selectedPaymentCard = card,
                        paymentMethodDisplay = payment.displayName ?: ""
                    )
                }
            }
            .onError {
                reduce { state.copy(isPaymentLoading = false) }
            }
    }

    private fun DefaultPaymentDto.toPaymentCard(): PaymentCard {
        val cardType = when (brand?.lowercase()) {
            "visa" -> CardType.VISA
            "mastercard" -> CardType.MASTERCARD
            else -> CardType.VISA
        }
        return PaymentCard(
            id = id,
            type = cardType,
            lastFourDigits = last4 ?: cardMask ?: "",
            brand = brand ?: "",
            displayName = displayName ?: "",
            isSelected = true
        )
    }

    private fun calculateSubtotal(quantity: Int, pricePerPiece: Double): Double {
        return (quantity * pricePerPiece).roundPrice()
    }

    private fun handleBackClicked() = intent {
        postSideEffect(ReserveSideEffect.NavigateBack)
    }

    private fun handlePlaceOrderClicked() = intent {
        if (state.paymentMethodDisplay.isEmpty()) {
            postSideEffect(ReserveSideEffect.ShowError("Please select a payment method"))
            return@intent
        }

        // Generate order number (6 digits)
        val orderNumber = Random.nextInt(100000, 999999).toString()

        // Create order info
        val orderInfo = OrderAccepted(
            orderNumber = orderNumber,
            venueName = state.venueName,
            pickupTime = state.pickupTime
        )

        // Place order and navigate to success screen
        postSideEffect(ReserveSideEffect.OrderPlaced(orderInfo))
    }

    private fun handlePaymentMethodClicked() = intent {
        reduce { state.copy(isPaymentSheetVisible = true, isPaymentCardsLoading = true) }
        loadAllPaymentCards()
    }

    private fun loadAllPaymentCards() = intent {
        offersRepository.getPaymentMethods()
            .onSuccess { methods ->
                val cards = methods.cards.map { it.toPaymentCard() }
                reduce {
                    state.copy(
                        isPaymentCardsLoading = false,
                        availablePaymentCards = cards
                    )
                }
            }
            .onError {
                reduce { state.copy(isPaymentCardsLoading = false) }
            }
    }

    private fun PaymentCardDto.toPaymentCard(): PaymentCard {
        val cardType = when (brand?.lowercase()) {
            "visa" -> CardType.VISA
            "mastercard" -> CardType.MASTERCARD
            else -> CardType.VISA
        }
        return PaymentCard(
            id = id,
            type = cardType,
            lastFourDigits = last4 ?: cardMask ?: "",
            brand = brand ?: "",
            displayName = displayName ?: "",
            isSelected = isDefault
        )
    }

    private fun handlePaymentSheetDismissed() = intent {
        reduce { state.copy(isPaymentSheetVisible = false) }
    }

    private fun handlePaymentCardSelected(card: PaymentCard) = intent {
        if (card.type == CardType.ADD_NEW) {
            return@intent
        }

        val updatedCards = state.availablePaymentCards.map {
            it.copy(isSelected = it.id == card.id)
        }

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

    private fun handleIncrementQuantity() = intent {
        if (state.quantity < state.itemsLeft) {
            val newQuantity = state.quantity + 1
            reduce {
                state.copy(
                    quantity = newQuantity,
                    subtotal = calculateSubtotal(newQuantity, state.pricePerPiece)
                )
            }
        }
    }

    private fun handleDecrementQuantity() = intent {
        if (state.quantity > 1) {
            val newQuantity = state.quantity - 1
            reduce {
                state.copy(
                    quantity = newQuantity,
                    subtotal = calculateSubtotal(newQuantity, state.pricePerPiece)
                )
            }
        }
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

    private fun handleSelectVoucherClicked() = intent {
        reduce { state.copy(isVoucherBottomSheetVisible = true) }
    }

    private fun handleVoucherSelected(voucher: Voucher) = intent {
        val updatedVouchers = state.availableVouchers.map {
            it.copy(isSelected = it.id == voucher.id)
        }
        reduce {
            state.copy(
                selectedVoucher = voucher,
                availableVouchers = updatedVouchers,
                isVoucherBottomSheetVisible = false
            )
        }
    }

    private fun handleVoucherBottomSheetDismissed() = intent {
        reduce { state.copy(isVoucherBottomSheetVisible = false) }
    }

    private fun handleAddNewCardClicked() = intent {
        reduce { state.copy(isRegisterCardLoading = true) }

        offersRepository.registerCard()
            .onSuccess { response ->
                reduce { state.copy(isRegisterCardLoading = false) }
                val url = response.redirectUrl ?: response.url
                if (url != null) {
                    postSideEffect(ReserveSideEffect.OpenRedirectUrl(url))
                }
                println("RegisterCard response: redirectUrl=${response.redirectUrl}, url=${response.url}, extra=${response.extra}")
            }
            .onError { error ->
                reduce { state.copy(isRegisterCardLoading = false) }
                postSideEffect(ReserveSideEffect.ShowError(error.message))
                println("RegisterCard error: ${error.message}")
            }
    }
}
