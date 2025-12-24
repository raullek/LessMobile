package az.less.mobile.presentation.merchant.history

import az.less.mobile.presentation.merchant.history.model.BranchOption
import az.less.mobile.presentation.merchant.history.model.IncomeHistory
import az.less.mobile.presentation.merchant.history.model.MonthOption

/**
 * State of the Income History Screen
 */
data class IncomeHistoryState(
    val selectedMonth: MonthOption? = null,
    val selectedBranch: BranchOption? = null,
    val availableMonths: List<MonthOption> = emptyList(),
    val availableBranches: List<BranchOption> = emptyList(),
    val incomeHistory: List<IncomeHistory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface IncomeHistorySideEffect {
    data class ShowError(val message: String) : IncomeHistorySideEffect
    data class ShowIncomePositionDetails(val positionId: String) : IncomeHistorySideEffect
    data class ShowMonthPicker(val months: List<MonthOption>) : IncomeHistorySideEffect
    data class ShowBranchPicker(val branches: List<BranchOption>) : IncomeHistorySideEffect
}

/**
 * User Intents/Actions
 */
sealed interface IncomeHistoryIntent {
    /** User clicked on download button */
    data object OnDownloadClicked : IncomeHistoryIntent
    
    /** User clicked on month filter button */
    data object OnMonthFilterClicked : IncomeHistoryIntent
    
    /** User selected a month */
    data class OnMonthSelected(val month: MonthOption) : IncomeHistoryIntent
    
    /** User clicked on branch filter button */
    data object OnBranchFilterClicked : IncomeHistoryIntent
    
    /** User selected a branch */
    data class OnBranchSelected(val branch: BranchOption) : IncomeHistoryIntent
    
    /** User clicked on an income position */
    data class OnIncomePositionClicked(val positionId: String) : IncomeHistoryIntent
    
    /** Pull to refresh */
    data object OnRefresh : IncomeHistoryIntent
}

