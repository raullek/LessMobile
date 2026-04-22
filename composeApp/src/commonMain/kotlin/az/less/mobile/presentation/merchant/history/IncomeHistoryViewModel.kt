package az.less.mobile.presentation.merchant.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.OrdersRepository
import az.less.mobile.presentation.merchant.history.model.toIncomeHistoryList
import az.less.mobile.utils.millisToApiDate
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
            is IncomeHistoryIntent.OnDownloadClicked -> Unit
            is IncomeHistoryIntent.OnDateRangeFilterClicked -> showDateRangePicker()
            is IncomeHistoryIntent.OnDateRangePickerDismiss -> dismissDateRangePicker()
            is IncomeHistoryIntent.OnDateRangeSelected -> handleDateRangeSelected(intent.startMillis, intent.endMillis)
            is IncomeHistoryIntent.OnDateRangeCleared -> handleDateRangeCleared()
            is IncomeHistoryIntent.OnIncomePositionClicked -> handleIncomePositionClicked(intent.positionId)
            is IncomeHistoryIntent.OnRefresh -> refreshIncomeHistory()
        }
    }

    private fun showDateRangePicker() = intent {
        reduce { state.copy(showDateRangePicker = true) }
    }

    private fun dismissDateRangePicker() = intent {
        reduce { state.copy(showDateRangePicker = false) }
    }

    private fun handleDateRangeSelected(startMillis: Long, endMillis: Long) = intent {
        reduce {
            state.copy(
                startDate = startMillis.millisToApiDate(),
                endDate = endMillis.millisToApiDate(),
                showDateRangePicker = false
            )
        }
        loadIncomeHistory()
    }

    private fun handleDateRangeCleared() = intent {
        reduce {
            state.copy(
                startDate = null,
                endDate = null,
                showDateRangePicker = false
            )
        }
        loadIncomeHistory()
    }

    private fun handleIncomePositionClicked(positionId: String) = intent {
        postSideEffect(IncomeHistorySideEffect.ShowIncomePositionDetails(positionId))
    }

    private fun loadIncomeHistory() = intent {
        reduce { state.copy(isLoading = true, error = null) }
        fetchHistory(state.startDate, state.endDate)
    }

    private fun refreshIncomeHistory() = intent {
        reduce { state.copy(isRefreshing = true, error = null) }
        fetchHistory(state.startDate, state.endDate)
    }

    private suspend fun fetchHistory(startDate: String?, endDate: String?) {
        ordersRepository.getOrderHistory(startDate = startDate, endDate = endDate)
            .onSuccess { data ->
                intent {
                    reduce {
                        state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            incomeHistory = data.toIncomeHistoryList()
                        )
                    }
                }
            }
            .onError { error ->
                intent {
                    reduce {
                        state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = error.message
                        )
                    }
                    postSideEffect(IncomeHistorySideEffect.ShowError(error.message))
                }
            }
    }
}
