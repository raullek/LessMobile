package az.less.mobile.presentation.client.reserve

import az.less.mobile.presentation.client.reserve.models.OrderAccepted
import az.less.mobile.presentation.client.reserve.models.PaymentCard
import az.less.mobile.presentation.client.reserve.models.Voucher

/**
 * Lot size info item for the info bottom sheet
 */
data class LotSizeInfo(
    val name: String,
    val description: String
)

/**
 * State of the Reserve Screen
 */
data class ReserveState(
    val lotName: String = "",
    val pickupTime: String = "",
    val lotDescription: String = "",
    val quantity: Int = 1,
    val itemsLeft: Int = 0,
    val pricePerPiece: Double = 0.0,
    val serviceFee: Double = 0.0,
    val subtotal: Double = 0.0,
    val paymentMethodDisplay: String = "",
    val selectedPaymentCard: PaymentCard? = null,
    val availablePaymentCards: List<PaymentCard> = emptyList(),
    val isLoading: Boolean = false,
    val isLotSizeInfoVisible: Boolean = false,
    val lotSizeInfoList: List<LotSizeInfo> = emptyList(),
    val isVoucherBottomSheetVisible: Boolean = false,
    val availableVouchers: List<Voucher> = emptyList(),
    val selectedVoucher: Voucher? = null
)


/**
 * Side Effects for navigation and one-time events
 */
sealed interface ReserveSideEffect {
    data object NavigateBack : ReserveSideEffect
    data object NavigateToPaymentMethods : ReserveSideEffect
    data object NavigateToAddressSelection : ReserveSideEffect
    data class ShowError(val message: String) : ReserveSideEffect
    data class OrderPlaced(val orderInfo: OrderAccepted) : ReserveSideEffect
    data class NavigateToMerchant(val merchantId: String) : ReserveSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface ReserveIntent {
    data object OnBackClicked : ReserveIntent
    data object OnPlaceOrderClicked : ReserveIntent
    data object OnPaymentMethodClicked : ReserveIntent
    data class OnPaymentCardSelected(val card: PaymentCard) : ReserveIntent
    data object OnIncrementQuantity : ReserveIntent
    data object OnDecrementQuantity : ReserveIntent
    data object OnSeeMoreDealsClicked : ReserveIntent
    data object OnLotInfoClicked : ReserveIntent
    data object OnLotInfoDismissed : ReserveIntent
    data object OnSelectVoucherClicked : ReserveIntent
    data class OnVoucherSelected(val voucher: Voucher) : ReserveIntent
    data object OnVoucherBottomSheetDismissed : ReserveIntent
}

