package az.less.mobile.presentation.main.offers

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.mobile.navigation.ReserveScreens
import az.less.mobile.presentation.main.offers.components.CategoryCard
import az.less.mobile.presentation.main.offers.components.FilterCategoryItem
import az.less.mobile.presentation.main.offers.components.OfferCard
import az.less.mobile.presentation.main.offers.components.OffersHeader
import az.less.mobile.presentation.main.offers.components.SearchFilterBar
import az.less.mobile.presentation.main.offers.components.SpecialDiscountPager
import az.less.mobile.presentation.reserve.ReserveScreen
import kotlinx.coroutines.launch
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_right_24dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful OffersScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffersScreen(
    viewModel: OffersViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()
    val scope = rememberCoroutineScope()

    // Reserve bottom sheet state
    var isReserveBottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    val reserveSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is OffersSideEffect.NavigateToOfferDetail -> {
                // Handle navigation to offer detail
                // navController.navigate("offer_detail/${sideEffect.offerId}")
            }
            is OffersSideEffect.NavigateToSearch -> {
                // Handle navigation to search screen
                navController.navigate("search")
            }
            is OffersSideEffect.NavigateToReserve -> {
                isReserveBottomSheetVisible = true
                scope.launch {
                    reserveSheetState.expand()
                }
            }
            is OffersSideEffect.ShowError -> {
                // Show error snackbar
            }
        }
    }

    // Render the stateless UI
    OffersScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )

    // Reserve Bottom Sheet
    ReserveScreen(
        isVisible = isReserveBottomSheetVisible,
        sheetState = reserveSheetState,
        viewModel = koinViewModel (),
        onDismiss = {
            scope.launch {
                reserveSheetState.hide()
            }.invokeOnCompletion {
                isReserveBottomSheetVisible = false
            }
        },
        onOrderPlaced = { orderInfo ->
            // Navigate to Order Accepted screen
            navController.navigate(
                ReserveScreens.OrderAccepted.createRoute(
                    orderNumber = orderInfo.orderNumber,
                    venueName = orderInfo.venueName,
                    pickupTime = orderInfo.pickupTime
                )
            )
        }
    )
}

/**
 * Stateless OffersScreen UI implementation
 * Pure UI that receives state and emits intents
 */
@Composable
fun OffersScreenContent(
    state: OffersState,
    onIntent: (OffersIntent) -> Unit,
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
        // Fixed Header Section
        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Header with User Avatar, Name, and Notification Buttons
        OffersHeader(
            userName = state.userName,
            onNotificationClick = { /* Handle notification */ },
            onMessageClick = { /* Handle message */ },
            userAvatarUrl = state.userAvatarUrl
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Search and Filter Bar
        SearchFilterBar(
            searchQuery = state.searchQuery,
            onSearchClick = {
                onIntent(OffersIntent.OnSearchClicked)
            },
            onFilterClick = { /* Handle filter */ }
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Scrollable Content
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Horizontal Categories Section
            item(key = "categories") {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(start = LessTheme.spacing.medium),
                    horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall)
                ) {
                    items(
                        items = state.categories,
                        key = { category -> category.id }
                    ) { category ->
                        CategoryCard(
                            title = category.title,
                            onClick = {
                                onIntent(OffersIntent.OnCategorySelected(category.id))
                            },
                            imageUrl = category.imageUrl,
                            testImage = category.testImage
                        )
                    }
                }
            }

            item(key = "categories_spacing") {
                Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge)) // 32 + 12 = 44dp
            }

            // Special Discounts Section with Horizontal Pager
            if (state.specialDiscounts.isNotEmpty()) {
                item(key = "special_discounts") {
                    SpecialDiscountPager(
                        items = state.specialDiscounts,
                        onItemClick = { item ->
                            onIntent(OffersIntent.OnOfferItemClicked(item.id))
                        }
                    )
                }
                
                item(key = "special_discounts_spacing") {
                    Spacer(modifier = Modifier.height(LessTheme.spacing.large))
                }
            }

            // Filter Segments Section
            if (state.filterSegments.isNotEmpty()) {
                item(key = "filter_segments") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = LessTheme.spacing.medium),
                        horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall)
                    ) {
                        state.filterSegments.forEach { segment ->
                            FilterCategoryItem(
                                modifier = Modifier.weight(1f),
                                text = segment.text,
                                icon = vectorResource(segment.icon),
                                iconTint = Color(segment.iconTint),
                                onItemClick = {
                                    onIntent(OffersIntent.OnFilterSegmentSelected(segment.id))
                                }
                            )
                        }
                    }
                }
                
                item(key = "filter_segments_spacing") {
                    Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
                }
            }

            // Dynamic Offer Sections (Top rated, Top picks, etc.)
            state.offerSections.forEach { section ->
                // Section Header
                item(key = "${section.id}_header") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = LessTheme.spacing.medium)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = section.title,
                                style = LessTheme.typography.body16Semibold,
                                color = LessTheme.colors.textIconsBlack
                            )

                            if (section.showSeeAll) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "See all",
                                        style = LessTheme.typography.body16Semibold,
                                        color = LessTheme.colors.textIconsBrand
                                    )
                                    Icon(
                                        painter = painterResource(Res.drawable.ic_chevron_right_24dp),
                                        contentDescription = "See all",
                                        tint = LessTheme.colors.textIconsBrand,
                                        modifier = Modifier.padding(start = LessTheme.spacing.xxxSmall)
                                    )
                                }
                            }
                        }
                    }
                }

                item(key = "${section.id}_spacing") {
                    Spacer(modifier = Modifier.height(LessTheme.spacing.small + LessTheme.spacing.xSmall)) // 12 + 8 = 20dp
                }

                // Section Items - Horizontal Scrolling Cards
                item(key = "${section.id}_items") {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium),
                        contentPadding = PaddingValues(horizontal = LessTheme.spacing.medium)
                    ) {
                        items(
                            items = section.items,
                            key = { item -> "${section.id}_${item.id}" }
                        ) { offerItem ->
                            OfferCard(
                                offerItem = offerItem,
                                onClick = {
                                    onIntent(OffersIntent.OnOfferItemClicked(offerItem.id))
                                }
                            )
                        }
                    }
                }

                item(key = "${section.id}_bottom_spacing") {
                    Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge + LessTheme.spacing.small)) // 32 + 12 = 44dp
                }
            }

            // Bottom spacing
            item(key = "bottom_spacing") {
                Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge))
            }
        }
    }
}


