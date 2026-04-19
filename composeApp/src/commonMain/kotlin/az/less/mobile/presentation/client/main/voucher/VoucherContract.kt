package az.less.mobile.presentation.client.main.voucher

import az.less.mobile.presentation.client.main.voucher.models.Voucher

data class VoucherState(
    val isLoading: Boolean = false,
    val vouchers: List<Voucher> = emptyList(),
    val error: String? = null
)

sealed interface VoucherSideEffect {
    data object NavigateBack : VoucherSideEffect
    data class ShowError(val message: String) : VoucherSideEffect
    data class ShowCopiedToast(val message: String) : VoucherSideEffect
}

sealed interface VoucherIntent {
    data object OnBackClicked : VoucherIntent
    data class OnCopyCodeClicked(val code: String) : VoucherIntent
    data object Retry : VoucherIntent
}
