package az.less.mobile.presentation.merchant.orders

import az.less.mobile.presentation.merchant.orders.model.BoughtBoxItem
import az.less.mobile.presentation.merchant.orders.model.CreatedBoxItem
import az.less.mobile.presentation.merchant.orders.model.MerchOrderTab

data class MerchOrdersState(
    val selectedTab: MerchOrderTab = MerchOrderTab.AWAITING_PICKUP,
    val boughtBoxes: List<BoughtBoxItem> = emptyList(),
    val createdBoxes: List<CreatedBoxItem> = emptyList(),
    val boughtTitle: String = "",
    val createdTitle: String = "",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

sealed interface MerchOrdersSideEffect {
    data class ShowError(val message: String) : MerchOrdersSideEffect
    data class ShowOrderDetails(val orderId: String) : MerchOrdersSideEffect
    data object OrderHandedOverSuccess : MerchOrdersSideEffect
    data object OrderCancelledSuccess : MerchOrdersSideEffect
}

sealed interface MerchOrdersIntent {
    data class OnTabSelected(val tab: MerchOrderTab) : MerchOrdersIntent
    data class OnHandedOverClicked(val orderId: String) : MerchOrdersIntent
    data class OnCancelLotClicked(val boxId: String) : MerchOrdersIntent
    data class OnOrderClicked(val orderId: String) : MerchOrdersIntent
    data object OnRefresh : MerchOrdersIntent
}
