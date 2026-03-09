package az.less.mobile.presentation.client.main.offers

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.mobile.navigation.ClientRoute
import az.less.mobile.presentation.client.main.offers.components.CategoryCard
import az.less.mobile.presentation.client.main.offers.components.FilterCategoryItem
import az.less.mobile.presentation.client.main.offers.components.OfferCard
import az.less.mobile.presentation.client.main.offers.components.OffersHeader
import az.less.mobile.presentation.client.main.offers.components.OffersScreenShimmer
import az.less.mobile.presentation.client.main.offers.components.SearchFilterBar
import az.less.mobile.presentation.client.main.offers.components.SpecialDiscountPager
import az.less.mobile.presentation.client.reserve.ReserveScreen
import az.less.mobile.presentation.maps.LocationPermissionHandler
import kotlinx.coroutines.launch
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_right_24dp
import lessmobile.composeapp.generated.resources.action_see_all
import lessmobile.composeapp.generated.resources.offers_greeting_guest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffersScreen(
    viewModel: OffersViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()
    val scope = rememberCoroutineScope()

    var isReserveBottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    var selectedOfferId by rememberSaveable { mutableStateOf("") }
    val reserveSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LocationPermissionHandler(
        onPermissionGranted = {
            viewModel.onIntent(OffersIntent.OnLocationPermissionChanged(granted = true))
        },
        onPermissionDenied = {
            viewModel.onIntent(OffersIntent.OnLocationPermissionChanged(granted = false))
        }
    ) {}

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is OffersSideEffect.NavigateToOfferDetail -> {
                navController.navigate(ClientRoute.OfferDetail(offerId = sideEffect.offerId))
            }

            is OffersSideEffect.NavigateToSearch -> {
                navController.navigate(ClientRoute.Search)
            }

            is OffersSideEffect.NavigateToCategoryOffers -> {
                navController.navigate(
                    ClientRoute.CategoryOffers(
                        categoryId = sideEffect.categoryId,
                        categoryType = sideEffect.categoryType,
                        categoryTitle = sideEffect.categoryTitle,
                        filtersJson = sideEffect.filtersJson
                    )
                )
            }

            is OffersSideEffect.NavigateToReserve -> {
                selectedOfferId = sideEffect.offerId
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

    OffersScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )

    ReserveScreen(
        isVisible = isReserveBottomSheetVisible,
        sheetState = reserveSheetState,
        offerId = selectedOfferId,
        onDismiss = {
            scope.launch {
                reserveSheetState.hide()
            }.invokeOnCompletion {
                isReserveBottomSheetVisible = false
            }
        },
        onOrderPlaced = { orderInfo ->
            navController.navigate(
                ClientRoute.OrderAccepted(
                    orderNumber = orderInfo.orderNumber,
                    venueName = orderInfo.venueName,
                    pickupTime = orderInfo.pickupTime
                )
            )
        },
        onNavigateToMerchant = { merchantId ->
            navController.navigate(ClientRoute.Merchant(merchantId = merchantId))
        },
        onNavigateToAddCardWebView = { url ->
            navController.navigate(
                ClientRoute.AddCardWebView(url = url, title = "Add Card")
            )
        }
    )
}

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
            .padding(bottom = LessTheme.size.large)
    ) {
        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        OffersHeader(
            userName = state.userName ?: stringResource(Res.string.offers_greeting_guest),
            onNotificationClick = { },
            onMessageClick = { },
            userAvatarUrl = state.userAvatarUrl
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        SearchFilterBar(
            searchQuery = state.searchQuery,
            onSearchClick = {
                onIntent(OffersIntent.OnSearchClicked)
            },
            onFilterClick = { }
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        if (state.isLoading) {
            OffersScreenShimmer(
                modifier = Modifier.fillMaxSize()
            )
        } else {
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { onIntent(OffersIntent.OnRefresh) },
                modifier = Modifier.fillMaxSize()
            ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // Categories Section
                if (state.categories.isNotEmpty()) {
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
                                    imageUrl = category.imageUrl
                                )
                            }
                        }
                    }

                    item(key = "categories_spacing") {
                        Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge))
                    }
                }

                // Special Discounts Section
                if (state.specialCategories.isNotEmpty()) {
                    item(key = "special_discounts") {
                        SpecialDiscountPager(
                            items = state.specialCategories,
                            onItemClick = { item ->
                                onIntent(OffersIntent.OnSpecialCategoryClicked(item.id))
                            }
                        )
                    }

                    item(key = "special_discounts_spacing") {
                        Spacer(modifier = Modifier.height(LessTheme.spacing.large))
                    }
                }

                // Homepage Buttons Section
                if (state.homepageButtons.isNotEmpty()) {
                    item(key = "homepage_buttons") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = LessTheme.spacing.medium),
                            horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall)
                        ) {
                            state.homepageButtons.forEach { button ->
                                FilterCategoryItem(
                                    modifier = Modifier.weight(1f),
                                    text = button.title,
                                    icon = button.icon?.let { vectorResource(it) },
                                    iconTint = button.iconTint?.let {
                                        androidx.compose.ui.graphics.Color(it)
                                    } ?: LessTheme.colors.textIconsBrand,
                                    onItemClick = {
                                        onIntent(OffersIntent.OnHomepageButtonClicked(button.id))
                                    }
                                )
                            }
                        }
                    }

                    item(key = "homepage_buttons_spacing") {
                        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
                    }
                }

                // Special Segments (Offer Sections)
                state.specialSegments.forEach { section ->
                    if (section.offers.isNotEmpty()) {
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
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.clickable {
                                                onIntent(OffersIntent.OnSeeAllClicked(section.id))
                                            }
                                        ) {
                                            Text(
                                                text = stringResource(Res.string.action_see_all),
                                                style = LessTheme.typography.body16Semibold,
                                                color = LessTheme.colors.textIconsBrand
                                            )
                                            Icon(
                                                painter = painterResource(Res.drawable.ic_chevron_right_24dp),
                                                contentDescription = stringResource(Res.string.action_see_all),
                                                tint = LessTheme.colors.textIconsBrand,
                                                modifier = Modifier.padding(start = LessTheme.spacing.xxxSmall)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item(key = "${section.id}_spacing") {
                            Spacer(modifier = Modifier.height(LessTheme.spacing.small + LessTheme.spacing.xSmall))
                        }

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
                                        },
                                        modifier = Modifier.width(277.dp)
                                    )
                                }
                            }
                        }

                        item(key = "${section.id}_bottom_spacing") {
                            Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge + LessTheme.spacing.small))
                        }
                    }
                }

                item(key = "bottom_spacing") {
                    Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge))
                }
            }
            }
        }
    }
}
