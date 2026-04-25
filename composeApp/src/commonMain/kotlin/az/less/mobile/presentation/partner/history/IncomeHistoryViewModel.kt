package az.less.mobile.presentation.partner.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.data.remote.model.AdminVenueDto
import az.less.mobile.domain.repository.OrdersRepository
import az.less.mobile.domain.repository.VenuesRepository
import az.less.mobile.presentation.partner.history.model.VenueOption
import az.less.mobile.presentation.partner.history.model.toIncomeHistoryList
import az.less.mobile.utils.millisToApiDate
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class IncomeHistoryViewModel(
    private val ordersRepository: OrdersRepository,
    private val venuesRepository: VenuesRepository
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
            is IncomeHistoryIntent.OnVenueFilterClicked -> handleVenueFilterClicked()
            is IncomeHistoryIntent.OnVenuePickerDismiss -> dismissVenuePicker()
            is IncomeHistoryIntent.OnVenueSelected -> handleVenueSelected(intent.venue)
            is IncomeHistoryIntent.OnVenueCleared -> handleVenueCleared()
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

    private fun handleVenueFilterClicked() = intent {
        if (state.isLoadingVenues) return@intent

        if (state.availableVenues.isNotEmpty()) {
            reduce { state.copy(showVenuePicker = true) }
            return@intent
        }

        reduce { state.copy(isLoadingVenues = true) }
        venuesRepository.getAllVenues(page = 1, limit = 100)
            .onSuccess { data ->
                reduce {
                    state.copy(
                        isLoadingVenues = false,
                        availableVenues = data.data.map { it.toVenueOption() },
                        showVenuePicker = true
                    )
                }
            }
            .onError { error ->
                reduce { state.copy(isLoadingVenues = false) }
                postSideEffect(IncomeHistorySideEffect.ShowError(error.message))
            }
    }

    private fun dismissVenuePicker() = intent {
        reduce { state.copy(showVenuePicker = false) }
    }

    private fun handleVenueSelected(venue: VenueOption) = intent {
        reduce {
            state.copy(
                selectedVenue = venue,
                showVenuePicker = false
            )
        }
        loadIncomeHistory()
    }

    private fun handleVenueCleared() = intent {
        reduce {
            state.copy(
                selectedVenue = null,
                showVenuePicker = false
            )
        }
        loadIncomeHistory()
    }

    private fun handleIncomePositionClicked(positionId: String) = intent {
        postSideEffect(IncomeHistorySideEffect.ShowIncomePositionDetails(positionId))
    }

    private fun loadIncomeHistory() = intent {
        reduce { state.copy(isLoading = true, error = null) }
        fetchHistory(state.startDate, state.endDate, state.selectedVenue?.id)
    }

    private fun refreshIncomeHistory() = intent {
        reduce { state.copy(isRefreshing = true, error = null) }
        fetchHistory(state.startDate, state.endDate, state.selectedVenue?.id)
    }

    private suspend fun fetchHistory(startDate: String?, endDate: String?, venueId: String?) {
        ordersRepository.getOrderHistory(
            startDate = startDate,
            endDate = endDate,
            venueId = venueId
        )
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

private fun AdminVenueDto.toVenueOption(): VenueOption = VenueOption(
    id = id,
    name = name,
    address = businessAddress.orEmpty()
)
