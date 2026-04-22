package az.less.mobile.presentation.merchant.history

import az.less.mobile.presentation.merchant.history.model.IncomeHistory

data class IncomeHistoryState(
    val startDate: String? = null,
    val endDate: String? = null,
    val incomeHistory: List<IncomeHistory> = emptyList(),
    val showDateRangePicker: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

sealed interface IncomeHistorySideEffect {
    data class ShowError(val message: String) : IncomeHistorySideEffect
    data class ShowIncomePositionDetails(val positionId: String) : IncomeHistorySideEffect
}

sealed interface IncomeHistoryIntent {
    data object OnDownloadClicked : IncomeHistoryIntent
    data object OnDateRangeFilterClicked : IncomeHistoryIntent
    data object OnDateRangePickerDismiss : IncomeHistoryIntent
    data class OnDateRangeSelected(val startMillis: Long, val endMillis: Long) : IncomeHistoryIntent
    data object OnDateRangeCleared : IncomeHistoryIntent
    data class OnIncomePositionClicked(val positionId: String) : IncomeHistoryIntent
    data object OnRefresh : IncomeHistoryIntent
}
