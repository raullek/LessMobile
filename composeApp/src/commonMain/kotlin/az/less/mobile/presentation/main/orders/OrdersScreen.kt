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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.SegmentOption
import az.less.designsystem.components.SegmentedButton
import az.less.mobile.presentation.main.orders.components.OrderItem
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

/**
 * Stateful OrdersScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    
    // Render the stateless UI
    OrdersScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
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
            .offset(y=LessTheme.spacing.xLarge)
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
                    text = "Cart"
                ),
                SegmentOption(
                    id = OrderTab.HISTORY.name,
                    text = "History"
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

