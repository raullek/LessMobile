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
import az.less.mobile.presentation.client.reserve.ContactSupportBottomSheet
import az.less.mobile.presentation.client.reserve.LeaveReviewBottomSheet
import az.less.mobile.presentation.client.reserve.PreviousOrderInfoBottomSheet
import az.less.mobile.presentation.client.reserve.ReserveInfoBottomSheet
import az.less.mobile.presentation.client.reserve.models.PreviousOrderInfo
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

    // Active-order ("Show me location") sheet state.
    var isReserveInfoVisible by remember { mutableStateOf(false) }
    var selectedReserveInfo by remember { mutableStateOf<ReserveInfo?>(null) }
    val reserveInfoSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    // Previous-order ("Leave review" / "Contact with support") sheet state —
    // separate sheet because the actions are unrelated to the active flow.
    var isPreviousOrderInfoVisible by remember { mutableStateOf(false) }
    var selectedPreviousOrderInfo by remember { mutableStateOf<PreviousOrderInfo?>(null) }
    val previousOrderSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    // Leave-review sheet state — opened from the previous-order sheet.
    var isLeaveReviewVisible by remember { mutableStateOf(false) }
    var leaveReviewVenueName by remember { mutableStateOf("") }
    val leaveReviewSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    // Contact-support sheet state — same DsListBottomSheet pattern as the
    // More-screen "Contact us" sheet; opened from the previous-order sheet.
    var isContactSupportVisible by remember { mutableStateOf(false) }

    // Collect side effects
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is OrdersSideEffect.ShowReserveInfo -> {
                val order = sideEffect.order
                if (order.isCompleted) {
                    selectedPreviousOrderInfo = PreviousOrderInfo(
                        boxTitle = order.title,
                        pickedUpOn = order.completedDate ?: order.pickupTimeFormatted,
                        reserveNumber = order.reserveNumber,
                        date = order.date,
                        pricePerPiece = order.pricePerPiece,
                        serviceFee = order.serviceFee,
                        subtotal = order.subtotalAmount,
                        orderId = order.id
                    )
                    isPreviousOrderInfoVisible = true
                    scope.launch { previousOrderSheetState.expand() }
                } else {
                    selectedReserveInfo = ReserveInfo(
                        venueName = order.title,
                        pickupTime = order.pickupTimeFormatted,
                        reserveNumber = order.reserveNumber,
                        date = order.date,
                        pricePerPiece = order.pricePerPiece,
                        serviceFee = order.serviceFee,
                        subtotal = order.subtotalAmount,
                        venueId = order.venueId
                    )
                    isReserveInfoVisible = true
                    scope.launch { reserveInfoSheetState.expand() }
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

    // Active-order info sheet — single "Show me location" CTA.
    selectedReserveInfo?.let { reserveInfo ->
        ReserveInfoBottomSheet(
            isVisible = isReserveInfoVisible,
            sheetState = reserveInfoSheetState,
            reserveInfo = reserveInfo,
            onShowLocationClicked = {
                if (reserveInfo.venueId.isNotBlank()) {
                    scope.launch { reserveInfoSheetState.hide() }
                        .invokeOnCompletion {
                            isReserveInfoVisible = false
                            navController.navigate(
                                ClientRoute.VenueMap(venueId = reserveInfo.venueId)
                            )
                        }
                }
            },
            onDismiss = {
                scope.launch { reserveInfoSheetState.hide() }
                    .invokeOnCompletion { isReserveInfoVisible = false }
            }
        )
    }

    // Previous-order info sheet — "Leave review" / "Contact with support".
    selectedPreviousOrderInfo?.let { info ->
        PreviousOrderInfoBottomSheet(
            isVisible = isPreviousOrderInfoVisible,
            sheetState = previousOrderSheetState,
            info = info,
            onLeaveReviewClicked = {
                // Hand off to the leave-review sheet: hide the current one
                // first so the user sees a clean transition rather than
                // two stacked sheets, then open the review sheet for the
                // same venue.
                val venueName = info.boxTitle
                scope.launch { previousOrderSheetState.hide() }
                    .invokeOnCompletion {
                        isPreviousOrderInfoVisible = false
                        leaveReviewVenueName = venueName
                        isLeaveReviewVisible = true
                        scope.launch { leaveReviewSheetState.expand() }
                    }
            },
            onContactSupportClicked = {
                // Same hand-off pattern as the leave-review path: hide the
                // previous-order sheet first, then open the support sheet.
                scope.launch { previousOrderSheetState.hide() }
                    .invokeOnCompletion {
                        isPreviousOrderInfoVisible = false
                        isContactSupportVisible = true
                    }
            },
            onDismiss = {
                scope.launch { previousOrderSheetState.hide() }
                    .invokeOnCompletion { isPreviousOrderInfoVisible = false }
            }
        )
    }

    // Contact-support sheet — list of social channels (Instagram, TikTok,
    // Facebook, Telegram, WhatsApp). Reuses [DsListBottomSheet], the same
    // primitive the More screen's "Contact us" sheet is built on, so the
    // row layout / icons stay consistent across the app.
    ContactSupportBottomSheet(
        isVisible = isContactSupportVisible,
        onChannelClick = { _ ->
            // TODO: launch the channel's URL once support links are wired.
            isContactSupportVisible = false
        },
        onDismiss = { isContactSupportVisible = false }
    )

    // Leave-review sheet — owns its own rating + comment local state.
    if (isLeaveReviewVisible) {
        LeaveReviewBottomSheet(
            isVisible = true,
            sheetState = leaveReviewSheetState,
            venueName = leaveReviewVenueName,
            onSubmit = { _, _ ->
                // TODO: post the review to the backend via a viewmodel intent
                // once the endpoint is wired. For now just dismiss the sheet.
                scope.launch { leaveReviewSheetState.hide() }
                    .invokeOnCompletion { isLeaveReviewVisible = false }
            },
            onDismiss = {
                scope.launch { leaveReviewSheetState.hide() }
                    .invokeOnCompletion { isLeaveReviewVisible = false }
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
