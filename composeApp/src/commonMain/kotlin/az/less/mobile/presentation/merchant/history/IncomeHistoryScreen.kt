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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
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
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful IncomeHistoryScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun IncomeHistoryScreen(
    navController: androidx.navigation.NavController,
    viewModel: IncomeHistoryViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    
    // Collect side effects
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is IncomeHistorySideEffect.ShowError -> {
                // TODO: Show error snackbar
            }
            is IncomeHistorySideEffect.ShowIncomePositionDetails -> {
                // TODO: Navigate to income position details
            }
            is IncomeHistorySideEffect.ShowMonthPicker -> {
                // TODO: Show month picker bottom sheet
            }
            is IncomeHistorySideEffect.ShowBranchPicker -> {
                // TODO: Show branch picker bottom sheet
            }
        }
    }
    
    // Render the stateless UI
    IncomeHistoryScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

/**
 * Stateless IncomeHistoryScreen UI implementation
 * Pure UI that receives state and emits intents
 */
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
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = LessTheme.spacing.medium),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "History",
                style = LessTheme.typography.title24Bold,
                color = LessTheme.colors.textIconsBlack
            )
        }
        
        // Filter buttons row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = LessTheme.spacing.medium,
                    vertical = LessTheme.spacing.small
                ),
            horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.small)
        ) {
            // Download icon button
            DownloadIconButton(
                onClick = { onIntent(IncomeHistoryIntent.OnDownloadClicked) }
            )
            
            // Month filter button
            FilterButton(
                text = state.selectedMonth?.displayName ?: "Select Month",
                onClick = { onIntent(IncomeHistoryIntent.OnMonthFilterClicked) },
                modifier = Modifier.weight(1f)
            )
            
            // Branch filter button
            FilterButton(
                text = state.selectedBranch?.name ?: "All branches",
                onClick = { onIntent(IncomeHistoryIntent.OnBranchFilterClicked) },
                modifier = Modifier.weight(1f)
            )
        }
        
        // Income history list
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = LessTheme.colors.textIconsBrand
                )
            }
        } else if (state.incomeHistory.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No income history found",
                    style = LessTheme.typography.body16Medium,
                    color = LessTheme.colors.textIconsGrey,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 0.dp,
                    end = 0.dp,
                    bottom = LessTheme.spacing.xLarge
                ),
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

/**
 * Income history group component showing date, income value, and list of positions
 */
@Composable
private fun IncomeHistoryGroup(
    incomeHistory: az.less.mobile.presentation.merchant.history.model.IncomeHistory,
    onPositionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Date header with total income value
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
        
        // Income positions
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            incomeHistory.incomePositions.forEachIndexed { index, position ->
                IncomePositionItem(
                    position = position,
                    onClick = { onPositionClick(position.id) }
                )
                
                // Add divider between positions (last position doesn't need divider)
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

