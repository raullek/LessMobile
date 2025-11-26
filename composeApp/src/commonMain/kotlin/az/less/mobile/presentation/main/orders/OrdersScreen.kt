package az.less.mobile.presentation.main.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.SegmentOption
import az.less.designsystem.components.SegmentedButton
import az.less.mobile.presentation.main.orders.components.OrderItem
import az.less.mobile.presentation.reserve.ReserveInfoBottomSheet
import az.less.mobile.presentation.reserve.models.ReserveInfo
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful OrdersScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    val scope = rememberCoroutineScope()
    
    // Reserve info bottom sheet state
    var isReserveInfoVisible by remember { mutableStateOf(false) }
    var selectedReserveInfo by remember { mutableStateOf<ReserveInfo?>(null) }
    val reserveInfoSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    
    // Collect side effects
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is OrdersSideEffect.ShowReserveInfo -> {
                // Convert CartItem to ReserveInfo
                selectedReserveInfo = ReserveInfo(
                    venueName = sideEffect.cartItem.title,
                    pickupTime = sideEffect.cartItem.pickupTime,
                    reserveNumber = sideEffect.cartItem.reserveNumber,
                    date = "12.05.2026", // TODO: Get from cart item or calculate
                    pricePerPiece = sideEffect.cartItem.price.toDoubleOrNull() ?: 0.0,
                    serviceFee = 0.20, // TODO: Get from cart item
                    subtotal = (sideEffect.cartItem.price.toDoubleOrNull() ?: 0.0) + 0.20
                )
                isReserveInfoVisible = true
                scope.launch {
                    reserveInfoSheetState.expand()
                }
            }
            is OrdersSideEffect.NavigateToCheckout -> {
                // Handle checkout navigation
            }
            is OrdersSideEffect.NavigateBack -> {
                // Handle back navigation
            }
            is OrdersSideEffect.ShowError -> {
                // Show error snackbar
            }
        }
    }
    
    // Render the stateless UI
    OrdersScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
    
    // Reserve Info Bottom Sheet
    selectedReserveInfo?.let { reserveInfo ->
        ReserveInfoBottomSheet(
            isVisible = isReserveInfoVisible,
            sheetState = reserveInfoSheetState,
            reserveInfo = reserveInfo,
            onShowLocationClicked = {
                // TODO: Navigate to map/location
            },
            onDismiss = {
                scope.launch {
                    reserveInfoSheetState.hide()
                }.invokeOnCompletion {
                    isReserveInfoVisible = false
                }
            }
        )
    }
}

/**
 * Stateless OrdersScreen UI implementation
 * Pure UI that receives state and emits intents
 */
@Composable
fun OrdersScreenContent(
    state: OrdersState,
    onIntent: (OrdersIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
            .padding(horizontal = LessTheme.spacing.medium)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = LessTheme.size.large) // Space for bottom nav bar
    ) {
        // Top spacing
        Spacer(modifier = Modifier.height(LessTheme.spacing.small))
        
        // Title
        Text(
            text = "Orders",
            style = LessTheme.typography.body16Semibold,
            color = LessTheme.colors.textIconsBlack,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.large)
        )
        
        // Spacing between title and segmented button
        Spacer(modifier = Modifier.height(LessTheme.spacing.large))
        
        // SegmentedButton
        SegmentedButton(
            options = listOf(
                SegmentOption(
                    id = OrderTab.CART.name,
                    text = "Active"
                ),
                SegmentOption(
                    id = OrderTab.HISTORY.name,
                    text = "Previous orders"
                )
            ),
            selectedOptionId = state.selectedTab.name,
            onOptionSelected = { optionId ->
                val tab = OrderTab.valueOf(optionId)
                onIntent(OrdersIntent.OnTabSelected(tab))
            },
            modifier = Modifier,
            selectedTextColor = LessTheme.colors.textIconsNested
        )
        
        // Spacing between segmented button and list
        Spacer(modifier = Modifier.height(LessTheme.spacing.large))
        
        // Order items list
        val items = when (state.selectedTab) {
            OrderTab.CART -> state.cartItems
            OrderTab.HISTORY -> state.historyItems
        }
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall),
        ) {
            items(
                items = items,
                key = { it.id }
            ) { item ->
                OrderItem(
                    item = item,
                    onClick = { onIntent(OrdersIntent.OnCartItemClicked(item.id)) },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Divider between items (except last)
                if (item != items.last()) {
                    HorizontalDivider(
                        color = LessTheme.colors.borderPrimary,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = LessTheme.spacing.medium)
                    )
                }
            }
        }
    }
}

