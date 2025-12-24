package az.less.mobile.presentation.merchant.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.merchant.history.model.BranchOption
import az.less.mobile.presentation.merchant.history.model.IncomeHistory
import az.less.mobile.presentation.merchant.history.model.IncomePosition
import az.less.mobile.presentation.merchant.history.model.MonthOption
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Income History Screen using Orbit MVI
 */
class IncomeHistoryViewModel : ViewModel(), ContainerHost<IncomeHistoryState, IncomeHistorySideEffect> {

    override val container: Container<IncomeHistoryState, IncomeHistorySideEffect> =
        viewModelScope.container(IncomeHistoryState())

    init {
        loadIncomeHistory()
        loadFilterOptions()
    }

    /**
     * Handle user intents
     */
    fun onIntent(intent: IncomeHistoryIntent) {
        when (intent) {
            is IncomeHistoryIntent.OnDownloadClicked -> handleDownloadClicked()
            is IncomeHistoryIntent.OnMonthFilterClicked -> handleMonthFilterClicked()
            is IncomeHistoryIntent.OnMonthSelected -> handleMonthSelected(intent.month)
            is IncomeHistoryIntent.OnBranchFilterClicked -> handleBranchFilterClicked()
            is IncomeHistoryIntent.OnBranchSelected -> handleBranchSelected(intent.branch)
            is IncomeHistoryIntent.OnIncomePositionClicked -> handleIncomePositionClicked(intent.positionId)
            is IncomeHistoryIntent.OnRefresh -> loadIncomeHistory()
        }
    }

    private fun handleDownloadClicked() = intent {
        // TODO: Implement download functionality (e.g., export income history to CSV/PDF)
    }

    private fun loadFilterOptions() = intent {
        // TODO: Replace with actual repository calls
        val months = getMockMonths()
        val branches = getMockBranches()
        
        reduce {
            state.copy(
                availableMonths = months,
                availableBranches = branches,
                selectedMonth = months.firstOrNull(),
                selectedBranch = branches.firstOrNull()
            )
        }
    }

    private fun loadIncomeHistory() = intent {
        reduce { state.copy(isLoading = true, error = null) }
        
        // TODO: Replace with actual repository call
        // Format: [incomeHistory{date, incomeValue, [incomePositions]}]
        val incomeHistory = getMockIncomeHistory()
        
        reduce {
            state.copy(
                isLoading = false,
                incomeHistory = incomeHistory
            )
        }
    }

    private fun handleMonthFilterClicked() = intent {
        postSideEffect(IncomeHistorySideEffect.ShowMonthPicker(state.availableMonths))
    }

    private fun handleMonthSelected(month: MonthOption) = intent {
        reduce { state.copy(selectedMonth = month) }
        loadIncomeHistory() // Reload income history with new filter
    }

    private fun handleBranchFilterClicked() = intent {
        postSideEffect(IncomeHistorySideEffect.ShowBranchPicker(state.availableBranches))
    }

    private fun handleBranchSelected(branch: BranchOption) = intent {
        reduce { state.copy(selectedBranch = branch) }
        loadIncomeHistory() // Reload income history with new filter
    }

    private fun handleIncomePositionClicked(positionId: String) = intent {
        postSideEffect(IncomeHistorySideEffect.ShowIncomePositionDetails(positionId))
    }

    // Mock data - replace with repository calls in real app
    // Format: [incomeHistory{date, incomeValue, [incomePositions]}]
    private fun getMockIncomeHistory(): List<IncomeHistory> {
        return listOf(
            IncomeHistory(
                date = "Today",
                incomeValue = "2 026,25",
                incomePositions = listOf(
                    IncomePosition(
                        id = "1",
                        customerName = "Mahammadali Pashayev",
                        customerInitials = "MP",
                        time = "12:34",
                        branchName = "Ahmdali mcdonalds",
                        amount = "2.24"
                    ),
                    IncomePosition(
                        id = "2",
                        customerName = "Mahammadali Pashayev",
                        customerInitials = "MP",
                        time = "12:34",
                        branchName = "Xalqlar mcdonalds",
                        amount = "2.24"
                    ),
                    IncomePosition(
                        id = "3",
                        customerName = "Mahammadali Pashayev",
                        customerInitials = "MP",
                        time = "12:34",
                        branchName = "Xalqlar mcdonalds",
                        amount = "2.24"
                    ),
                    IncomePosition(
                        id = "4",
                        customerName = "Mahammadali Pashayev",
                        customerInitials = "MP",
                        time = "12:34",
                        branchName = "Xalqlar mcdonalds",
                        amount = "2.24"
                    ),
                    IncomePosition(
                        id = "5",
                        customerName = "Mahammadali Pashayev",
                        customerInitials = "MP",
                        time = "12:34",
                        branchName = "Xalqlar mcdonalds",
                        amount = "2.24"
                    ),
                    IncomePosition(
                        id = "6",
                        customerName = "Mahammadali Pashayev",
                        customerInitials = "MP",
                        time = "12:34",
                        branchName = "Xalqlar mcdonalds",
                        amount = "2.24"
                    )
                )
            ),
            IncomeHistory(
                date = "10 Mart",
                incomeValue = "126,25",
                incomePositions = listOf(
                    IncomePosition(
                        id = "7",
                        customerName = "Mahammadali Pashayev",
                        customerInitials = "MP",
                        time = "12:34",
                        branchName = "Xalqlar mcdonalds",
                        amount = "2.24"
                    ),
                    IncomePosition(
                        id = "8",
                        customerName = "Mahammadali Pashayev",
                        customerInitials = "MP",
                        time = "12:34",
                        branchName = "Ahmdali mcdonalds",
                        amount = "2.24"
                    ),
                    IncomePosition(
                        id = "9",
                        customerName = "Mahammadali Pashayev",
                        customerInitials = "MP",
                        time = "12:34",
                        branchName = "Ahmdali mcdonalds",
                        amount = "2.24"
                    ),
                    IncomePosition(
                        id = "10",
                        customerName = "Mahammadali Pashayev",
                        customerInitials = "MP",
                        time = "12:34",
                        branchName = "Xalqlar mcdonalds",
                        amount = "2.24"
                    )
                )
            ),
            IncomeHistory(
                date = "10 Fevral",
                incomeValue = "126,25",
                incomePositions = listOf(
                    IncomePosition(
                        id = "11",
                        customerName = "Mahammadali Pashayev",
                        customerInitials = "MP",
                        time = "12:34",
                        branchName = "Xalqlar mcdonalds",
                        amount = "2.24"
                    ),
                    IncomePosition(
                        id = "12",
                        customerName = "Mahammadali Pashayev",
                        customerInitials = "MP",
                        time = "12:34",
                        branchName = "Ahmdali mcdonalds",
                        amount = "2.24"
                    ),
                    IncomePosition(
                        id = "13",
                        customerName = "Mahammadali Pashayev",
                        customerInitials = "MP",
                        time = "12:34",
                        branchName = "Xalqlar mcdonalds",
                        amount = "2.24"
                    ),
                    IncomePosition(
                        id = "14",
                        customerName = "Mahammadali Pashayev",
                        customerInitials = "MP",
                        time = "12:34",
                        branchName = "Ahmdali mcdonalds",
                        amount = "2.24"
                    )
                )
            )
        )
    }

    private fun getMockMonths(): List<MonthOption> {
        return listOf(
            MonthOption("2024-09", "September", "2024-09"),
            MonthOption("2024-08", "August", "2024-08"),
            MonthOption("2024-07", "July", "2024-07"),
            MonthOption("2024-06", "June", "2024-06")
        )
    }

    private fun getMockBranches(): List<BranchOption> {
        return listOf(
            BranchOption("all", "All branches"),
            BranchOption("1", "Ahmdali mcdonalds"),
            BranchOption("2", "Xalqlar mcdonalds")
        )
    }
}

