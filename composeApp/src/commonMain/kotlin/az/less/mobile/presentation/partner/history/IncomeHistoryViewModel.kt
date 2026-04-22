package az.less.mobile.presentation.partner.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.OrdersRepository
import az.less.mobile.presentation.partner.history.model.MonthOption
import az.less.mobile.presentation.partner.history.model.toIncomeHistoryList
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class IncomeHistoryViewModel(
    private val ordersRepository: OrdersRepository
) : ViewModel(), ContainerHost<IncomeHistoryState, IncomeHistorySideEffect> {

    override val container: Container<IncomeHistoryState, IncomeHistorySideEffect> =
        viewModelScope.container(IncomeHistoryState())

    init {
        loadIncomeHistory()
    }

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
        // TODO: Implement download functionality
    }

    private fun loadIncomeHistory() = intent {
        reduce { state.copy(isLoading = true, error = null) }

        val startDate = state.selectedMonth?.startDate
        val endDate = state.selectedMonth?.endDate

        ordersRepository.getOrderHistory(
            startDate = startDate,
            endDate = endDate
        )
            .onSuccess { data ->
                reduce {
                    state.copy(
                        isLoading = false,
                        incomeHistory = data.toIncomeHistoryList()
                    )
                }
            }
            .onError { error ->
                reduce {
                    state.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
                postSideEffect(IncomeHistorySideEffect.ShowError(error.message))
            }
    }

    private fun handleMonthFilterClicked() = intent {
        // TODO: Show calendar/month picker widget (will be implemented later)
        postSideEffect(IncomeHistorySideEffect.ShowMonthPicker(state.availableMonths))
    }

    private fun handleMonthSelected(month: MonthOption) = intent {
        reduce { state.copy(selectedMonth = month) }
        loadIncomeHistory()
    }

    private fun handleBranchFilterClicked() = intent {
        // TODO: Implement branch filter when branch-specific data is available
    }

    private fun handleBranchSelected(branch: az.less.mobile.presentation.partner.history.model.BranchOption) = intent {
        reduce { state.copy(selectedBranch = branch) }
    }

    private fun handleIncomePositionClicked(positionId: String) = intent {
        postSideEffect(IncomeHistorySideEffect.ShowIncomePositionDetails(positionId))
    }
}
