package az.less.mobile.presentation.merchant.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.merchant.history.components.DownloadIconButton
import az.less.mobile.presentation.merchant.history.components.FilterButton
import az.less.mobile.presentation.merchant.history.components.IncomePositionItem
import az.less.mobile.utils.apiDateToMillis
import az.less.mobile.utils.currentTimeMillis
import az.less.mobile.utils.millisToApiDate
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.action_cancel
import lessmobile.composeapp.generated.resources.action_clear
import lessmobile.composeapp.generated.resources.action_ok
import lessmobile.composeapp.generated.resources.history_empty
import lessmobile.composeapp.generated.resources.history_select_date_range
import lessmobile.composeapp.generated.resources.history_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun IncomeHistoryScreen(
    navController: androidx.navigation.NavController,
    viewModel: IncomeHistoryViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is IncomeHistorySideEffect.ShowError -> Unit
            is IncomeHistorySideEffect.ShowIncomePositionDetails -> Unit
        }
    }

    IncomeHistoryScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeHistoryScreenContent(
    state: IncomeHistoryState,
    onIntent: (IncomeHistoryIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = LessTheme.spacing.medium),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(Res.string.history_title),
                style = LessTheme.typography.title24Bold,
                color = LessTheme.colors.textIconsBlack
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = LessTheme.spacing.medium,
                    vertical = LessTheme.spacing.small
                ),
            horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.small)
        ) {
            DownloadIconButton(
                onClick = { onIntent(IncomeHistoryIntent.OnDownloadClicked) }
            )

            val dateRangeLabel = formatDateRangeLabel(state.startDate, state.endDate)
                ?: stringResource(Res.string.history_select_date_range)
            FilterButton(
                text = dateRangeLabel,
                onClick = { onIntent(IncomeHistoryIntent.OnDateRangeFilterClicked) },
                modifier = Modifier.weight(1f)
            )
        }

        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onIntent(IncomeHistoryIntent.OnRefresh) },
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                state.isLoading && state.incomeHistory.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = LessTheme.colors.textIconsBrand)
                    }
                }

                state.incomeHistory.isEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = LessTheme.spacing.xLarge)
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(Res.string.history_empty),
                                    style = LessTheme.typography.body16Medium,
                                    color = LessTheme.colors.textIconsGrey,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = LessTheme.spacing.xLarge),
                        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
                    ) {
                        items(
                            items = state.incomeHistory,
                            key = { it.date }
                        ) { incomeHistory ->
                            IncomeHistoryGroup(
                                incomeHistory = incomeHistory,
                                onPositionClick = { positionId ->
                                    onIntent(IncomeHistoryIntent.OnIncomePositionClicked(positionId))
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (state.showDateRangePicker) {
        DateRangePickerDialog(
            initialStartMillis = state.startDate?.apiDateToMillis(),
            initialEndMillis = state.endDate?.apiDateToMillis(),
            hasActiveSelection = state.startDate != null && state.endDate != null,
            onDismiss = { onIntent(IncomeHistoryIntent.OnDateRangePickerDismiss) },
            onClear = { onIntent(IncomeHistoryIntent.OnDateRangeCleared) },
            onConfirm = { start, end ->
                onIntent(IncomeHistoryIntent.OnDateRangeSelected(start, end))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRangePickerDialog(
    initialStartMillis: Long?,
    initialEndMillis: Long?,
    hasActiveSelection: Boolean,
    onDismiss: () -> Unit,
    onClear: () -> Unit,
    onConfirm: (Long, Long) -> Unit
) {
    val todayMillis = currentTimeMillis()
    val todayApi = todayMillis.millisToApiDate()
    val currentYear = todayApi.substring(0, 4).toInt()
    val todayStartMillis = todayApi.apiDateToMillis() ?: todayMillis
    val minMillis = "${currentYear - 2}${todayApi.substring(4)}".apiDateToMillis()
        ?: (todayStartMillis - 2L * 366 * 24 * 60 * 60 * 1000)

    val selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean =
            utcTimeMillis in minMillis..todayStartMillis

        override fun isSelectableYear(year: Int): Boolean =
            year in (currentYear - 2)..currentYear
    }

    val rangeState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialStartMillis,
        initialSelectedEndDateMillis = initialEndMillis,
        yearRange = (currentYear - 2)..currentYear,
        selectableDates = selectableDates
    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val start = rangeState.selectedStartDateMillis
                    val end = rangeState.selectedEndDateMillis
                    if (start != null && end != null) onConfirm(start, end) else onDismiss()
                }
            ) { Text(stringResource(Res.string.action_ok)) }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall)) {
                if (hasActiveSelection) {
                    TextButton(onClick = onClear) {
                        Text(stringResource(Res.string.action_clear))
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text(stringResource(Res.string.action_cancel))
                }
            }
        }
    ) {
        DateRangePicker(
            state = rangeState,
            title = null,
            headline = null,
            showModeToggle = false,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun formatDateRangeLabel(startDate: String?, endDate: String?): String? {
    if (startDate.isNullOrEmpty() || endDate.isNullOrEmpty()) return null
    return "${startDate.apiDateToDisplay()} - ${endDate.apiDateToDisplay()}"
}

private fun String.apiDateToDisplay(): String {
    val parts = split("-")
    if (parts.size != 3) return this
    return "${parts[2]}.${parts[1]}.${parts[0]}"
}

@Composable
private fun IncomeHistoryGroup(
    incomeHistory: az.less.mobile.presentation.merchant.history.model.IncomeHistory,
    onPositionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = LessTheme.spacing.medium,
                    vertical = LessTheme.spacing.xSmall
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = incomeHistory.date,
                style = LessTheme.typography.body14Medium,
                color = LessTheme.colors.textIconsGrey
            )
            Text(
                text = "+${incomeHistory.incomeValue} ₼",
                style = LessTheme.typography.body14Medium,
                color = LessTheme.colors.textIconsGrey
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            incomeHistory.incomePositions.forEachIndexed { index, position ->
                IncomePositionItem(
                    position = position,
                    onClick = { onPositionClick(position.id) }
                )
                if (index < incomeHistory.incomePositions.size - 1) {
                    HorizontalDivider(
                        color = LessTheme.colors.backgroundSecond,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}
