package az.less.mobile.presentation.client.main.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.SegmentOption
import az.less.designsystem.components.SegmentedButton
import az.less.mobile.navigation.ClientRoute
import az.less.mobile.presentation.client.main.orders.components.OrderItem
import az.less.mobile.presentation.client.main.orders.components.OrdersEmptyState
import az.less.mobile.presentation.client.main.orders.components.OrdersNotLoggedInState
import az.less.mobile.presentation.client.main.orders.components.OrdersScreenShimmer
import az.less.mobile.presentation.client.main.orders.models.Order
import az.less.mobile.presentation.client.reserve.ReserveInfoBottomSheet
import az.less.mobile.presentation.client.reserve.models.ReserveInfo
import kotlinx.coroutines.launch
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.orders_title
import lessmobile.composeapp.generated.resources.orders_tab_active
import lessmobile.composeapp.generated.resources.orders_tab_previous
import org.jetbrains.compose.resources.stringResource
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
    navController: NavController,
    viewModel: OrdersViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    val scope = rememberCoroutineScope()
    val activeOrders = viewModel.activeOrders.collectAsLazyPagingItems()
    val previousOrders = viewModel.previousOrders.collectAsLazyPagingItems()

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
                val order = sideEffect.order
                selectedReserveInfo = ReserveInfo(
                    venueName = order.title,
                    pickupTime = order.pickupTimeFormatted,
                    reserveNumber = order.reserveNumber,
                    date = order.date,
                    pricePerPiece = order.pricePerPiece,
                    serviceFee = order.serviceFee,
                    subtotal = order.subtotalAmount
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
                navController.popBackStack()
            }
            is OrdersSideEffect.ShowError -> {
                // Show error snackbar
            }
            is OrdersSideEffect.NavigateToOffers -> {
                navController.navigate(ClientRoute.Offers)
            }
            is OrdersSideEffect.NavigateToMore -> {
                navController.navigate(ClientRoute.More)
            }
        }
    }

    // Render the stateless UI
    OrdersScreenContent(
        state = state,
        activeOrders = activeOrders,
        previousOrders = previousOrders,
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
    activeOrders: LazyPagingItems<Order>,
    previousOrders: LazyPagingItems<Order>,
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
            text = stringResource(Res.string.orders_title),
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
                    id = OrderTab.ACTIVE.name,
                    text = stringResource(Res.string.orders_tab_active)
                ),
                SegmentOption(
                    id = OrderTab.PREVIOUS.name,
                    text = stringResource(Res.string.orders_tab_previous)
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

        when {
            !state.isLoggedIn -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    OrdersNotLoggedInState(
                        onSignInClick = {
                            onIntent(OrdersIntent.OnSignInClicked)
                        }
                    )
                }
            }

            else -> {
                val pagingItems = when (state.selectedTab) {
                    OrderTab.ACTIVE -> activeOrders
                    OrderTab.PREVIOUS -> previousOrders
                }

                when {
                    pagingItems.loadState.refresh is LoadState.Loading -> {
                        OrdersScreenShimmer(
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    pagingItems.itemCount == 0 && pagingItems.loadState.refresh is LoadState.NotLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            OrdersEmptyState(
                                onExploreOffersClick = {
                                    onIntent(OrdersIntent.OnExploreOffersClicked)
                                }
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall),
                            contentPadding = PaddingValues(bottom = LessTheme.size.huge)
                        ) {
                            items(
                                count = pagingItems.itemCount,
                                key = pagingItems.itemKey { it.id }
                            ) { index ->
                                val order = pagingItems[index]
                                if (order != null) {
                                    OrderItem(
                                        item = order,
                                        onClick = {
                                            onIntent(OrdersIntent.OnOrderClicked(order))
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    // Divider between items (except last)
                                    if (index < pagingItems.itemCount - 1) {
                                        HorizontalDivider(
                                            color = LessTheme.colors.borderPrimary,
                                            thickness = 1.dp,
                                            modifier = Modifier.padding(vertical = LessTheme.spacing.medium)
                                        )
                                    }
                                }
                            }

                            if (pagingItems.loadState.append is LoadState.Loading) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(LessTheme.spacing.medium),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            color = LessTheme.colors.textIconsBrand
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
