package az.less.mobile.presentation.merchant.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.AnimatedToast
import az.less.designsystem.components.SegmentOption
import az.less.designsystem.components.SegmentedButton
import az.less.designsystem.components.ToastType
import az.less.mobile.presentation.merchant.orders.components.BoughtBoxCard
import az.less.mobile.presentation.merchant.orders.components.CreatedBoxCard
import az.less.mobile.presentation.merchant.orders.components.MerchBoxesEmptyState
import az.less.mobile.presentation.merchant.orders.model.MerchOrderTab
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.merch_orders_awaiting_pickup
import lessmobile.composeapp.generated.resources.merch_orders_awaiting_purchase
import lessmobile.composeapp.generated.resources.merch_orders_lot_added
import lessmobile.composeapp.generated.resources.merch_orders_lot_remove_info
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun MerchOrdersScreen(
    navController: NavController,
    viewModel: MerchOrdersViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    var showHandedOverToast by remember { mutableStateOf(false) }

    LaunchedEffect(showHandedOverToast) {
        if (showHandedOverToast) {
            delay(3000)
            showHandedOverToast = false
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MerchOrdersSideEffect.ShowError -> { }
            is MerchOrdersSideEffect.ShowOrderDetails -> { }
            is MerchOrdersSideEffect.OrderHandedOverSuccess -> {
                showHandedOverToast = true
            }
            is MerchOrdersSideEffect.OrderCancelledSuccess -> { }
        }
    }

    MerchOrdersScreenContent(
        state = state,
        onIntent = viewModel::onIntent,
        showHandedOverToast = showHandedOverToast
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MerchOrdersScreenContent(
    state: MerchOrdersState,
    onIntent: (MerchOrdersIntent) -> Unit,
    modifier: Modifier = Modifier,
    showHandedOverToast: Boolean = false
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LessTheme.colors.backgroundSecond)
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(bottom = LessTheme.size.large)
        ) {
            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            // Tab selector
            SegmentedButton(
                options = listOf(
                    SegmentOption(
                        id = MerchOrderTab.AWAITING_PICKUP.name,
                        text = stringResource(Res.string.merch_orders_awaiting_pickup)
                    ),
                    SegmentOption(
                        id = MerchOrderTab.AWAITING_PURCHASE.name,
                        text = stringResource(Res.string.merch_orders_awaiting_purchase)
                    )
                ),
                selectedOptionId = state.selectedTab.name,
                onOptionSelected = { optionId ->
                    val tab = MerchOrderTab.valueOf(optionId)
                    onIntent(MerchOrdersIntent.OnTabSelected(tab))
                },
                modifier = Modifier.padding(horizontal = LessTheme.spacing.medium),
                selectedTextColor = LessTheme.colors.textIconsNested
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.large))

            // Pull to refresh content
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { onIntent(MerchOrdersIntent.OnRefresh) },
                modifier = Modifier.fillMaxSize()
            ) {
                when (state.selectedTab) {
                    MerchOrderTab.AWAITING_PICKUP -> {
                        if (state.boughtBoxes.isEmpty() && !state.isLoading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                MerchBoxesEmptyState()
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(
                                    start = LessTheme.spacing.medium,
                                    end = LessTheme.spacing.medium,
                                    bottom = LessTheme.spacing.xLarge
                                ),
                                verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
                            ) {
                                items(
                                    items = state.boughtBoxes,
                                    key = { it.id }
                                ) { item ->
                                    BoughtBoxCard(
                                        item = item,
                                        onCardClick = {
                                            onIntent(MerchOrdersIntent.OnOrderClicked(item.orderId))
                                        },
                                        onHandedOverClick = {
                                            onIntent(MerchOrdersIntent.OnHandedOverClicked(item.id))
                                        }
                                    )
                                }
                            }
                        }
                    }

                    MerchOrderTab.AWAITING_PURCHASE -> {
                        if (state.createdBoxes.isEmpty() && !state.isLoading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                MerchBoxesEmptyState()
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(
                                    start = LessTheme.spacing.medium,
                                    end = LessTheme.spacing.medium,
                                    bottom = LessTheme.spacing.xLarge
                                ),
                                verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
                            ) {
                                items(
                                    items = state.createdBoxes,
                                    key = { it.id }
                                ) { item ->
                                    CreatedBoxCard(
                                        item = item,
                                        onCardClick = {
                                            onIntent(MerchOrdersIntent.OnOrderClicked(item.id))
                                        },
                                        onCancelClick = {
                                            onIntent(MerchOrdersIntent.OnCancelLotClicked(item.id))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Success Toast
        AnimatedToast(
            visible = showHandedOverToast,
            title = stringResource(Res.string.merch_orders_lot_added),
            subtitle = stringResource(Res.string.merch_orders_lot_remove_info),
            type = ToastType.Success,
            showGradientScrim = true,
            contentPadding = PaddingValues(
                start = LessTheme.spacing.medium,
                end = LessTheme.spacing.medium,
                top = LessTheme.spacing.medium
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}
