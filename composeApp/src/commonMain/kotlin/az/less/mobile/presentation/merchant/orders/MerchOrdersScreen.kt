package az.less.mobile.presentation.merchant.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.AnimatedToast
import az.less.designsystem.components.ToastType
import az.less.mobile.presentation.merchant.orders.components.BoughtBoxCard
import az.less.mobile.presentation.merchant.orders.components.CreatedBoxCard
import az.less.mobile.presentation.merchant.orders.components.MerchOrdersShimmer
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.mobile.navigation.MerchantRoute
import az.less.mobile.presentation.merchant.orders.components.MerchBoxesEmptyState
import az.less.mobile.presentation.merchant.orders.model.MerchOrderTab
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.error_generic
import lessmobile.composeapp.generated.resources.merch_orders_bought
import lessmobile.composeapp.generated.resources.merch_orders_placed_lots
import lessmobile.composeapp.generated.resources.merch_orders_create_lot
import lessmobile.composeapp.generated.resources.merch_orders_delivered
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
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastType by remember { mutableStateOf(ToastType.Success) }

    val deliveredMsg = stringResource(Res.string.merch_orders_delivered)
    val errorMsg = stringResource(Res.string.error_generic)

    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(3000)
            toastMessage = null
        }
    }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    LaunchedEffect(savedStateHandle) {
        savedStateHandle
            ?.getStateFlow<String?>(MERCH_ORDERS_SELECT_TAB_KEY, null)
            ?.collect { tabName ->
                if (tabName != null) {
                    if (tabName == MerchOrderTab.AWAITING_PURCHASE.name) {
                        viewModel.onIntent(MerchOrdersIntent.OnLotAdded)
                    } else {
                        val tab = runCatching { MerchOrderTab.valueOf(tabName) }.getOrNull()
                        if (tab != null) {
                            viewModel.onIntent(MerchOrdersIntent.OnTabSelected(tab))
                        }
                    }
                    savedStateHandle[MERCH_ORDERS_SELECT_TAB_KEY] = null
                }
            }
    }

    val createdBoxesListState = rememberLazyListState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MerchOrdersSideEffect.ShowError -> {
                toastType = ToastType.Error
                toastMessage = sideEffect.message.ifEmpty { errorMsg }
            }
            is MerchOrdersSideEffect.ShowOrderDetails -> { }
            is MerchOrdersSideEffect.OrderHandedOverSuccess -> {
                toastType = ToastType.Success
                toastMessage = deliveredMsg
            }
            is MerchOrdersSideEffect.OrderCancelledSuccess -> {
                toastType = ToastType.Success
                toastMessage = deliveredMsg
            }
            is MerchOrdersSideEffect.ScrollCreatedBoxesToTop -> {
                createdBoxesListState.animateScrollToItem(0)
            }
        }
    }

    MerchOrdersScreenContent(
        state = state,
        onIntent = viewModel::onIntent,
        toastMessage = toastMessage,
        toastType = toastType,
        createdBoxesListState = createdBoxesListState,
        onCreateLotClick = {
            navController.navigate(MerchantRoute.AddLot)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MerchOrdersScreenContent(
    state: MerchOrdersState,
    onIntent: (MerchOrdersIntent) -> Unit,
    modifier: Modifier = Modifier,
    toastMessage: String? = null,
    toastType: ToastType = ToastType.Success,
    createdBoxesListState: androidx.compose.foundation.lazy.LazyListState = rememberLazyListState(),
    onCreateLotClick: () -> Unit = {}
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

            // Tab selector - pill buttons per design
            Row(
                modifier = Modifier.padding(horizontal = LessTheme.spacing.medium),
                horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall)
            ) {
                OrdersTabPill(
                    text = stringResource(Res.string.merch_orders_bought),
                    isSelected = state.selectedTab == MerchOrderTab.AWAITING_PICKUP,
                    onClick = { onIntent(MerchOrdersIntent.OnTabSelected(MerchOrderTab.AWAITING_PICKUP)) }
                )
                OrdersTabPill(
                    text = stringResource(Res.string.merch_orders_placed_lots),
                    isSelected = state.selectedTab == MerchOrderTab.AWAITING_PURCHASE,
                    onClick = { onIntent(MerchOrdersIntent.OnTabSelected(MerchOrderTab.AWAITING_PURCHASE)) }
                )
            }

            Spacer(modifier = Modifier.height(LessTheme.spacing.large))

            // Pull to refresh content
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { onIntent(MerchOrdersIntent.OnRefresh) },
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    state.isLoading -> {
                        MerchOrdersShimmer(modifier = Modifier.fillMaxSize())
                    }

                    state.selectedTab == MerchOrderTab.AWAITING_PICKUP -> {
                        if (state.boughtBoxes.isEmpty()) {
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
                                        },
                                        isDelivering = state.deliveringOrderId == item.id
                                    )
                                }
                            }
                        }
                    }

                    state.selectedTab == MerchOrderTab.AWAITING_PURCHASE -> {
                        if (state.isCreatedBoxesReloading) {
                            MerchOrdersShimmer(modifier = Modifier.fillMaxSize())
                        } else if (state.createdBoxes.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                MerchBoxesEmptyState()
                            }
                        } else {
                            LazyColumn(
                                state = createdBoxesListState,
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

                                // Create Lot button
                                item(key = "createLotButton") {
                                    Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))
                                    DsButton(
                                        text = stringResource(Res.string.merch_orders_create_lot),
                                        onClick = onCreateLotClick,
                                        modifier = Modifier.fillMaxWidth(),
                                        variant = ButtonVariant.Primary,
                                        size = ButtonSize.Large
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Toast
        AnimatedToast(
            visible = toastMessage != null,
            title = toastMessage ?: "",
            type = toastType,
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

/**
 * Pill-shaped tab button matching Figma design.
 * Active: green bg, white text. Inactive: white bg, border, black text.
 */
@Composable
private fun OrdersTabPill(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(LessTheme.radius.medium)

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .then(
                if (isSelected) {
                    Modifier.background(LessTheme.colors.elementsPrimaryBrand, shape)
                } else {
                    Modifier
                        .background(LessTheme.colors.backgroundPrimary, shape)
                        .border(1.dp, LessTheme.colors.borderPrimary, shape)
                }
            )
            .clickable { onClick() }
            .padding(horizontal = LessTheme.spacing.xLarge, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = LessTheme.typography.body14Semibold,
            color = if (isSelected) {
                LessTheme.colors.textIconsNested
            } else {
                LessTheme.colors.textIconsBlack
            }
        )
    }
}
