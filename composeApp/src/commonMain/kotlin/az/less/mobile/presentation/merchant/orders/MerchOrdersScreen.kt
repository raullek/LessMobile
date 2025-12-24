package az.less.mobile.presentation.merchant.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.SegmentOption
import az.less.designsystem.components.SegmentedButton
import az.less.mobile.presentation.merchant.orders.components.MerchOrderCard
import az.less.mobile.presentation.merchant.orders.model.MerchOrderTab
import az.less.mobile.presentation.merchant.orders.model.OrderButtonState
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful MerchOrdersScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun MerchOrdersScreen(
    navController: NavController,
    viewModel: MerchOrdersViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    
    // Collect side effects
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MerchOrdersSideEffect.ShowError -> {
                // TODO: Show error snackbar
            }
            is MerchOrdersSideEffect.ShowOrderDetails -> {
                // TODO: Navigate to order details
            }
            is MerchOrdersSideEffect.OrderHandedOverSuccess -> {
                // TODO: Show success message
            }
            is MerchOrdersSideEffect.OrderCancelledSuccess -> {
                // TODO: Show success message
            }
        }
    }
    
    // Render the stateless UI
    MerchOrdersScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

/**
 * Stateless MerchOrdersScreen UI implementation
 * Pure UI that receives state and emits intents
 */
@Composable
fun MerchOrdersScreenContent(
    state: MerchOrdersState,
    onIntent: (MerchOrdersIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = LessTheme.size.large) // Space for bottom nav bar
    ) {
        // Top spacing
        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
        
        // SegmentedButton - "Awaiting Pickup" / "Awaiting Purchase"
        SegmentedButton(
            options = listOf(
                SegmentOption(
                    id = MerchOrderTab.AWAITING_PICKUP.name,
                    text = "Awaiting Pickup"
                ),
                SegmentOption(
                    id = MerchOrderTab.AWAITING_PURCHASE.name,
                    text = "Awaiting Purchase"
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
        
        // Spacing between segmented button and list
        Spacer(modifier = Modifier.height(LessTheme.spacing.large))
        
        // Order items list
        val orders = when (state.selectedTab) {
            MerchOrderTab.AWAITING_PICKUP -> state.awaitingPickupOrders
            MerchOrderTab.AWAITING_PURCHASE -> state.awaitingPurchaseOrders
        }
        
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
                items = orders,
                key = { it.id }
            ) { order ->
                MerchOrderCard(
                    order = order,
                    onCardClick = {
                        onIntent(MerchOrdersIntent.OnOrderClicked(order.id))
                    },
                    onButtonClick = {
                        when (order.buttonState) {
                            OrderButtonState.HANDED_OVER -> {
                                onIntent(MerchOrdersIntent.OnHandedOverClicked(order.id))
                            }
                            OrderButtonState.CANCEL_LOT -> {
                                onIntent(MerchOrdersIntent.OnCancelLotClicked(order.id))
                            }
                            OrderButtonState.CANCELLATION_TIME_ENDED -> {
                                // Disabled state - no action
                            }
                        }
                    }
                )
            }
        }
    }
}
