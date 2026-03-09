package az.less.mobile.presentation.client.main.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavController
import az.less.mobile.navigation.ClientRoute
import az.less.mobile.presentation.client.main.explore.components.FilterBottomSheet
import az.less.mobile.presentation.client.main.explore.components.FilterChip
import az.less.mobile.presentation.client.main.explore.components.MerchantSlotsRow
import az.less.mobile.presentation.client.main.explore.models.QuickFilter
import az.less.mobile.presentation.client.reserve.ReserveScreen
import kotlinx.coroutines.launch
import az.less.mobile.presentation.maps.GoogleMaps
import az.less.mobile.presentation.maps.LocationPermissionHandler
import az.less.mobile.presentation.maps.models.CameraPosition
import az.less.mobile.presentation.maps.models.LatLong
import az.less.mobile.presentation.maps.models.MapType
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful ExploreScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    navController: NavController,
    viewModel: ExploreViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    val scope = rememberCoroutineScope()

    var isReserveBottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    var selectedOfferId by rememberSaveable { mutableStateOf("") }
    val reserveSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ExploreSideEffect.NavigateToVenueDetail -> {
                // Handle navigation to venue detail
            }
            is ExploreSideEffect.NavigateToSearch -> {
                // Handle navigation to search screen
            }
            is ExploreSideEffect.NavigateToFilter -> {
                // Handle navigation to filter screen
            }
            is ExploreSideEffect.ShowError -> {
                // Show error snackbar
            }
            is ExploreSideEffect.NavigateToReserve -> {
                selectedOfferId = sideEffect.offerId
                isReserveBottomSheetVisible = true
                scope.launch {
                    reserveSheetState.expand()
                }
            }
        }
    }

    // Render the stateless UI
    ExploreScreenContent(
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

/**
 * Stateless ExploreScreen UI implementation
 * Pure UI that receives state and emits intents
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreenContent(
    state: ExploreState,
    onIntent: (ExploreIntent) -> Unit,

    modifier: Modifier = Modifier
) {
    var locationPermissionGranted by remember { mutableStateOf(false) }
    var locationPermissionDenied by remember { mutableStateOf(false) }
    var shouldRenderMap by remember { mutableStateOf(false) }

    // Delay map rendering to allow tab animation to complete
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1000)
        shouldRenderMap = true
    }

    // Filter bottom sheet state
    val filterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    // Update location permission in state
    LaunchedEffect(locationPermissionGranted) {
        onIntent(ExploreIntent.OnLocationPermissionChanged(locationPermissionGranted))
    }
    
    // Handle filter sheet visibility
    LaunchedEffect(state.isFilterSheetVisible) {
        if (state.isFilterSheetVisible) {
            coroutineScope.launch {
                filterSheetState.expand()
            }
        } else {
            coroutineScope.launch {
                filterSheetState.hide()
            }
        }
    }
    
    // Handle sheet dismissal
    LaunchedEffect(filterSheetState.isVisible) {
        if (!filterSheetState.isVisible && state.isFilterSheetVisible) {
            onIntent(ExploreIntent.OnFilterSheetDismissed)
        }
    }

    LocationPermissionHandler(
        onPermissionGranted = {
            locationPermissionGranted = true
            locationPermissionDenied = false
            println("✅ Location permission granted")
        },
        onPermissionDenied = {
            locationPermissionGranted = false
            locationPermissionDenied = true
            println("❌ Location permission denied")
        }
    ) {
        // Map is full screen
        Box(modifier = modifier.fillMaxSize()) {
            // TODO: Add BackHandler to dismiss merchant slots on back press
            // BackHandler(enabled = state.selectedMerchantSlots.isNotEmpty()) {
            //     onIntent(ExploreIntent.OnDismissMerchantSlots)
            // }

            // Google Maps - render after delay to avoid blocking tab switch
            if (shouldRenderMap) {
                GoogleMaps(
                    modifier = Modifier.fillMaxSize(),
                    shouldSetInitialCameraPosition = CameraPosition(
                        target = LatLong(40.4093, 49.8671), // Center on Baku
                        zoom = 13f
                    ),
                    mapType = MapType.NORMAL,
                    isZoomControlsVisible = false,
                    isCompassVisible = true,
                    isTrackingEnabled = locationPermissionGranted,
                    markers = state.markers, // Pass markers from state
                    onMarkerInfoClick = { marker ->
                        // Extract venue ID from marker tag if available
                        val venueId = marker.tag as? String ?: marker.id
                        onIntent(ExploreIntent.OnMapMarkerClicked(venueId))
                    },
                    onFindMeButtonClick = null, // Remove recenter location button
                    shouldCenterCameraOnLatLong = state.selectedMarkerPosition, // Center camera on selected marker
                    onDidCenterCameraOnLatLong = {
                        // Clear the position after camera has centered
                        onIntent(ExploreIntent.OnDidCenterCameraOnMarker)
                    }
                )
            }

            // Loading overlay while map initializes
            if (!shouldRenderMap) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LessTheme.colors.backgroundPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = LessTheme.colors.textIconsBrand
                    )
                }
            }

            // Top filter buttons bar - horizontally scrollable
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(
                        top = LessTheme.spacing.medium,
                    ),
                horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.small),
                contentPadding = PaddingValues(start = LessTheme.spacing.medium)
            ) {
                items(
                    items = state.filterItems,
                    key = { it.id }
                ) { filterItem ->
                    FilterChip(
                        text = filterItem.text,
                        iconType = filterItem.iconType,
                        isSelected = filterItem.isSelected,
                        onClick = {
                            when (filterItem.quickFilter) {
                                QuickFilter.FILTER_BUTTON -> {
                                    onIntent(ExploreIntent.OnFilterClicked)
                                }

                                else -> {
                                    onIntent(
                                        ExploreIntent.OnFilterItemClicked(
                                            filterItem.quickFilter
                                        )
                                    )
                                }
                            }
                        }
                    )
                }
            }
            
            // Merchant slots row at bottom when marker is selected
            if (state.selectedMerchantSlots.isNotEmpty()) {
                MerchantSlotsRow(
                    slots = state.selectedMerchantSlots,
                    merchantName = state.selectedMerchantName,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    onSlotClick = { slotId ->
                        onIntent(ExploreIntent.OnSlotClicked(slotId))
                    }
                )
            }
            
            // Filter Bottom Sheet
            FilterBottomSheet(
                isVisible = state.isFilterSheetVisible,
                sheetState = filterSheetState,
                filterData = state.filterData,
                onFilterOptionClicked = { categoryId, optionId ->
                    onIntent(
                        ExploreIntent.OnFilterOptionClicked(
                            categoryId,
                            optionId
                        )
                    )
                },
                onDismiss = {
                    onIntent(ExploreIntent.OnFilterSheetDismissed)
                },
                onApplyFilters = {
                    onIntent(ExploreIntent.OnApplyFilters)
                }
            )
        }
    }
}
