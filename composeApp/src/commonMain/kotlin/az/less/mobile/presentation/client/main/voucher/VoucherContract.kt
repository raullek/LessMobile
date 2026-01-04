package az.less.mobile.presentation.client.main.voucher

import az.less.mobile.presentation.client.main.voucher.models.Voucher

/**
 * State of the Voucher Screen
 */
data class VoucherState(
    val isLoading: Boolean = false,
    val vouchers: List<Voucher> = emptyList(),
    val referralCode: String = ""
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface VoucherSideEffect {
    data object NavigateBack : VoucherSideEffect
    data class ShowError(val message: String) : VoucherSideEffect
    data class ShowCopiedToast(val message: String) : VoucherSideEffect
    data object ShareReferralCode : VoucherSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface VoucherIntent {
    data object OnBackClicked : VoucherIntent
    data class OnCopyCodeClicked(val code: String) : VoucherIntent
    data object OnShareClicked : VoucherIntent
}
