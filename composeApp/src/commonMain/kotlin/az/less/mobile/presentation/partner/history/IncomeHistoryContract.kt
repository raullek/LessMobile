package az.less.mobile.presentation.partner.history

import az.less.mobile.presentation.partner.history.model.IncomeHistory
import az.less.mobile.presentation.partner.history.model.VenueOption

/**
 * State of the Income History Screen
 */
data class IncomeHistoryState(
    val startDate: String? = null,
    val endDate: String? = null,
    val selectedVenue: VenueOption? = null,
    val availableVenues: List<VenueOption> = emptyList(),
    val incomeHistory: List<IncomeHistory> = emptyList(),
    val showDateRangePicker: Boolean = false,
    val showVenuePicker: Boolean = false,
    val isLoadingVenues: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface IncomeHistorySideEffect {
    data class ShowError(val message: String) : IncomeHistorySideEffect
    data class ShowIncomePositionDetails(val positionId: String) : IncomeHistorySideEffect
}

/**
 * User Intents/Actions
 */
sealed interface IncomeHistoryIntent {
    data object OnDownloadClicked : IncomeHistoryIntent
    data object OnDateRangeFilterClicked : IncomeHistoryIntent
    data object OnDateRangePickerDismiss : IncomeHistoryIntent
    data class OnDateRangeSelected(val startMillis: Long, val endMillis: Long) : IncomeHistoryIntent
    data object OnDateRangeCleared : IncomeHistoryIntent
    data object OnVenueFilterClicked : IncomeHistoryIntent
    data object OnVenuePickerDismiss : IncomeHistoryIntent
    data class OnVenueSelected(val venue: VenueOption) : IncomeHistoryIntent
    data object OnVenueCleared : IncomeHistoryIntent
    data class OnIncomePositionClicked(val positionId: String) : IncomeHistoryIntent
    data object OnRefresh : IncomeHistoryIntent
}
